package com.fernando.ms.likes.app.application.services.proxy;

import com.fernando.ms.likes.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.likes.app.application.ports.output.LikePersistencePort;
import com.fernando.ms.likes.app.application.services.strategy.like.ITargetTypeStrategy;

import java.util.List;

public class ProcessFactory {
    private ProcessFactory() {
    }

    public static IProcess validateSaveLike(LikePersistencePort likePersistencePort, ExternalUserOutputPort externalUserOutputPort, List<ITargetTypeStrategy> targetTypeStrategyList) {
        return new RuleSaveLikeProxy(likePersistencePort,externalUserOutputPort,targetTypeStrategyList);
    }
}
