package org.superbiz.moviefun.blobstore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
public class Blob {
    public String name;
    public InputStream inputStream;
    public String contentType;
}