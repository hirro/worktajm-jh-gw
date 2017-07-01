package com.worktajm.gw.cucumber.stepdefs;

import com.worktajm.gw.WorktajmGwApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = WorktajmGwApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
