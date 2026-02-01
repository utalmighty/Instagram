package com.huduk.sos.SOS.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.huduk.sos.SOS.dto.MessageResponse;

public interface SOSService {
    
    public MessageResponse<String> save(MultipartFile file) throws IOException;
    public Resource fetch(String assetId) throws IOException;
}
