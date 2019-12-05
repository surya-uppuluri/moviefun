package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.String.format;

@Component
public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {
        InputStream inputStream = blob.getInputStream();
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        File targetFile = new File(blob.getName());
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        File coverFile = new File(name);
        if (coverFile.exists()) {
            Blob blob = new Blob();
            blob.setName(name);
            InputStream inputStream = new FileInputStream(coverFile);
            blob.setContentType(new Tika().detect(inputStream));
            blob.setInputStream(inputStream);
            return Optional.of(blob);
        }
        return null;
    }


    /*public Optional<Blob> get(long albumId) throws IOException {

        File coverFile = new File(name);
        if (coverFile.exists()) {
            Blob blob = new Blob();
            blob.setName(name);
            InputStream inputStream = new FileInputStream(coverFile);
            blob.setContentType(new Tika().detect(inputStream));
            blob.setInputStream(inputStream);
            return Optional.of(blob);
        }
        return null;
    }*/

    @Override
    public void deleteAll() {

    }




   /* public File getCoverFile(long albumId) {
        String coverFileName = format("covers/%d", albumId);
        return new File(coverFileName);
    }*/

   /* public Path getExistingCoverPath(long albumId) throws URISyntaxException {
        File coverFile = getCoverFile(albumId);
        Path coverFilePath;

        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {
            coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
        }

        return coverFilePath;
    }*/
}
