package com.dragonsofmugloar.adventure;

import com.dragonsofmugloar.adventure.service.GameService;
import org.junit.jupiter.api.Test;

class AdventureApplicationTest {

    @Test
    void runWithValidArgsShouldStartGame() {
        String[] args = {"3"};

        MockedGameService gameService = new MockedGameService();
        AdventureApplication app = new TestableAdventureApplication(3, gameService);
        app.run(args);

        assert gameService.started;
        assert gameService.numberOfGames == 3;
    }

    @Test
    void runWithInvalidArgsShouldNotStartGame() {
        String[] args = {"abc"};

        MockedGameService gameService = new MockedGameService();
        AdventureApplication app = new TestableAdventureApplication(-1, gameService);
        app.run(args);

        assert !gameService.started;
    }

    @Test
    void runWithZeroOrNegativeShouldNotStartGame() {
        String[] args = {"0"};

        MockedGameService gameService = new MockedGameService();
        AdventureApplication app = new TestableAdventureApplication(0, gameService);
        app.run(args);

        assert !gameService.started;
    }

    static class TestableAdventureApplication extends AdventureApplication {
        private final int mockGameCount;
        private final GameService mockGameService;

        TestableAdventureApplication(int mockGameCount, GameService mockGameService) {
            this.mockGameCount = mockGameCount;
            this.mockGameService = mockGameService;
        }

        @Override
        public void run(String... args) {
            int numberOfGames = getNumberOfGames(args);
            if (numberOfGames <= 0) return;
            mockGameService.startGames(numberOfGames);
        }

        protected int getNumberOfGames(String[] args) {
            return mockGameCount;
        }
    }

    static class MockedGameService extends GameService {
        boolean started = false;
        int numberOfGames = 0;

        public MockedGameService() {
            super(null);
        }

        @Override
        public void startGames(int numOfGames) {
            this.started = true;
            this.numberOfGames = numOfGames;
        }
    }
}
