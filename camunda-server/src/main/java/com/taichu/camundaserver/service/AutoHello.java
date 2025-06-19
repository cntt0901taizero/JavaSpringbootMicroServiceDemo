package com.taichu.camundaserver.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import javax.ws.rs.ApplicationPath;
import java.util.Objects;

@Named
public class AutoHello implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String LoiChao = (String) delegateExecution.getVariable("LoiChao");
        String QuocTich = (String) delegateExecution.getVariable("QuocTich");
        String GoodLuck = "";
        if (Objects.equals(QuocTich, "american")) {
            GoodLuck = "Good luck!";
        } else {
            GoodLuck = "Good luck to you!";
        }
        delegateExecution.setVariable("goodLuck", GoodLuck);
    }
}
