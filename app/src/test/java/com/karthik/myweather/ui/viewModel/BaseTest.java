package com.karthik.myweather.ui.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;

public class BaseTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

}
