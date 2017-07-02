package com.worktajm.gw.cucumber.stepdefs;

import com.worktajm.gw.WorktajmApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = WorktajmApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
