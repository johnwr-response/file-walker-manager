package no.responseweb.imagearchive.filewalkermanager.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.responseweb.imagearchive.filestoredbservice.domain.StatusWalker;
import no.responseweb.imagearchive.filestoredbservice.mappers.FileStoreMapper;
import no.responseweb.imagearchive.filestoredbservice.repositories.FileStoreRepository;
import no.responseweb.imagearchive.filestoredbservice.repositories.StatusWalkerRepository;
import no.responseweb.imagearchive.filewalkermanager.config.JmsConfig;
import no.responseweb.imagearchive.filewalkermanager.config.ResponseWalkerStatusProperties;
import no.responseweb.imagearchive.model.WalkerJobDto;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileWalkerJobProducer {

    private final JmsTemplate jmsTemplate;
    private final FileStoreRepository fileStoreRepository;
    private final StatusWalkerRepository statusWalkerRepository;
    private final FileStoreMapper fileStoreMapper;
    private final ResponseWalkerStatusProperties responseWalkerStatusProperties;

    @Scheduled(fixedDelayString = "${response.walker.scheduling.fixed-delay-time}", initialDelayString = "${response.walker.scheduling.initial-delay-time}")
    public void pushJobsToWalkers() {
        jmsTemplate.setPubSubDomain(true);
        fileStoreRepository.findAll().forEach(fileStore -> {
            log.debug("Test : {}, {}, {}", fileStore.getNickname(), fileStore.getLatestRefresh(), responseWalkerStatusProperties.getGlobalStoreRefreshMinutes());
            if ((fileStore.getLatestRefresh()==null ? LocalDateTime.MIN : fileStore.getLatestRefresh()).isBefore(LocalDateTime.now().minusMinutes(responseWalkerStatusProperties.getGlobalStoreRefreshMinutes()))) {
                StatusWalker availableWalker = statusWalkerRepository.findFirstByFileStoreIdAndReadyIsTrue(fileStore.getId());
                log.info("Send job to available walker for {}: {}", fileStore.getNickname(), availableWalker.getWalkerInstanceToken());
                jmsTemplate.convertAndSend(JmsConfig.FILE_STORE_WALKER_JOB_TOPIC,
                        WalkerJobDto.builder()
                                .walkerInstanceToken(availableWalker.getWalkerInstanceToken())
                                .fileStoreDto(fileStoreMapper.fileStoreToFileStoreDto(fileStore))
                                .build());
            }
        });
    }

}
