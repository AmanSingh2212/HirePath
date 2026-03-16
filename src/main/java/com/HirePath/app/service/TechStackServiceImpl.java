package com.HirePath.app.service;

import com.HirePath.app.entity.TechStack;
import com.HirePath.app.entity.User;
import com.HirePath.app.repository.TechStackRepository;
import com.HirePath.app.repository.UserRepository;
import com.HirePath.app.request.TechStackRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class TechStackServiceImpl implements TechStackService{

    private final TechStackRepository techStackRepository;
    private final UserRepository userRepository;

    public TechStackServiceImpl(TechStackRepository techStackRepository,
                                UserRepository userRepository) {
        this.techStackRepository = techStackRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public TechStack addTechStack(TechStackRequest techStackRequest, User user) throws Exception {

        // ✅ Validate request
        if (techStackRequest.getTechStacks() == null || techStackRequest.getTechStacks().isEmpty()) {
            throw new Exception("TechStack names cannot be empty");
        }

        // ✅ Re-fetch managed user within transaction
        User freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("User not found"));

        // ✅ Update existing TechStack or create new one (avoids duplicate records)
        TechStack techStack = freshUser.getTechStacks();
        if (techStack == null) {
            techStack = new TechStack();
        }

        // ✅ Use authenticated user, not request user
        techStack.setNames(techStackRequest.getTechStacks());
        techStack.setUser(freshUser);

        // ✅ Save TechStack FIRST to get its ID
        TechStack savedTechStack = techStackRepository.save(techStack);

        // ✅ Then link to user and save
        freshUser.setTechStacks(savedTechStack);
        userRepository.save(freshUser);

        return savedTechStack;
    }


    @Override
    public TechStack findById(Long techStackId) throws Exception {

        Optional<TechStack> techStack = techStackRepository.findById(techStackId);

        if(techStack.isEmpty())
        {
            throw new Exception("User not found with the given id");
        }

        return techStack.get();
    }

    @Override
    public TechStack addTech(User user, String techName) throws Exception{

        TechStack techStack = techStackRepository.findByUser(user);

        Optional<String> name = techStack.getNames().stream().filter(a -> Objects.equals(a, techName))
                                          .findFirst();

        if(name.isPresent())
        {
            throw new Exception("Tech is already present");
        }

        techStack.getNames().add(techName);

        return techStackRepository.save(techStack);

    }

    @Override
    public TechStack removeTech(User user, String techName) throws Exception{

        TechStack techStack = techStackRepository.findByUser(user);

        Optional<String> name = techStack.getNames().stream().filter(a -> Objects.equals(a, techName))
                .findFirst();

        if(name.isEmpty())
        {
            throw new Exception("Tech is not present");
        }

        techStack.getNames().remove(techName);

        return techStackRepository.save(techStack);

    }


}
