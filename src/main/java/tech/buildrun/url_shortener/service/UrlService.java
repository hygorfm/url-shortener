package tech.buildrun.url_shortener.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sqids.Sqids;
import tech.buildrun.url_shortener.entity.UrlEntity;
import tech.buildrun.url_shortener.repository.UrlRepository;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UrlService {

    private static final String CUSTOM_ALPHABET = "X49C7K2WDSA3Q8RE6ZJVPTN5FGYBMH";

    private final Sqids sqids = Sqids.builder()
                                     .alphabet(CUSTOM_ALPHABET)
                                     .minLength(5)
                                     .build();

    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(final UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // Using Apache Commons Lang lib
    public String generateAlphanumericId() {
        String id;
        do {
            id = RandomStringUtils.secure().nextAlphanumeric(5, 10);
        } while(urlRepository.existsById(id));

        return id;
    }

    // Using Sqids lib
    public String generateUniqueCode(Long sequentialId) {
        return sqids.encode(Arrays.asList(sequentialId));
    }

    public long getOriginalId(String code) {
        return sqids.decode(code).get(0);
    }


    public void save(UrlEntity urlEntity) {
        urlRepository.save(urlEntity);
    }


    public Optional<UrlEntity> findById(String id) {
        return this.urlRepository.findById(id);
    }

}
