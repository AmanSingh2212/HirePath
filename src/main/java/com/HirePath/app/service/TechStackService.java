package com.HirePath.app.service;

import com.HirePath.app.entity.TechStack;
import com.HirePath.app.entity.User;
import com.HirePath.app.request.TechStackRequest;

import java.util.List;

public interface TechStackService {

    public TechStack addTechStack(TechStackRequest techStackRequest, User user) throws Exception;

    public TechStack findById(Long techStackId) throws Exception;

    public TechStack addTech(User user, String techName) throws Exception;

    public TechStack removeTech(User user, String techName) throws Exception;

}
