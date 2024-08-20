package com.alcanl.app;

import com.alcanl.app.controller.StarterFrameController;
import com.alcanl.app.form.StarterForm;

class Application {
    public static void run(String[] args)
    {
        new StarterFrameController(new StarterForm());
    }
}
