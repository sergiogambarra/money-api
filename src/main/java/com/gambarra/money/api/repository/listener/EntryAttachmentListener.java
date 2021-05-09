package com.gambarra.money.api.repository.listener;

import com.gambarra.money.api.MoneyApiApplication;
import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.storage.S3;
import org.springframework.util.StringUtils;

import javax.persistence.PostLoad;

public class EntryAttachmentListener {

    @PostLoad
    public void postLoad(Entry entry) {
        if (StringUtils.hasText(entry.getAttachment())){
            S3 s3 = MoneyApiApplication.getBean(S3.class);
            entry.setUrlAttachment(s3.configureuUrl(entry.getAttachment()));
        }
    }
}
