package ru.homecredit.microservice.dao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homecredit.microservice.dao.entity.Microservice;
import ru.homecredit.microservice.dao.repository.MicroserviceRepo;
import ru.homecredit.microservice.dao.service.IMicroserviceDaoService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MicroserviceDaoService implements IMicroserviceDaoService {
    private final MicroserviceRepo microserviceRepo;

    @Override
    @Transactional(readOnly = true)
    public Stream<Microservice> getAllMicroservices() {
        //todo
        // это не надо
        // return microserviceRepo.findAllToStream();
        Iterable<Microservice> all = microserviceRepo.findAll();
        return StreamSupport.stream(all.spliterator(), false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctEnvDescs() {
        return getAllMicroservices()
                .filter(distinctByKey(Microservice::getEnvDesc))
                .map(Microservice::getEnvDesc)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Microservice> getServicesByEnvDesc(String env) {
        //todo
        // это не надо
        // return microserviceRepo.getByEnvDesc(env);
        return microserviceRepo.findByEnvDesc(env);
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
