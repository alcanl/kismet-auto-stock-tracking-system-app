package com.alcanl.app;

import com.alcanl.app.controller.StarterFrameController;

class Application {
    public static void run(String[] args)
    {
        new StarterFrameController(new StarterForm());
    }
}
