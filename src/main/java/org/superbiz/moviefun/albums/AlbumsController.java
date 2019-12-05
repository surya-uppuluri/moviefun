package org.superbiz.moviefun.albums;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.S3Store;
import org.superbiz.moviefun.blobstore.Blob;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;

@Controller
@RequestMapping("/albums")
public class AlbumsController {

    private S3Store s3Store;

    private final AlbumsBean albumsBean;

    public AlbumsController(AlbumsBean albumsBean, S3Store s3Store) {
        this.albumsBean = albumsBean;
        this.s3Store = s3Store;
    }


    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        model.put("album", albumsBean.find(albumId));
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {

        Blob blob = new Blob();
        blob.setName(format("covers/%d", albumId));

        InputStream inputStream = new BufferedInputStream(uploadedFile.getInputStream());
        blob.setContentType(new Tika().detect(inputStream));
        blob.setInputStream(inputStream);
        s3Store.put(blob);
        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException, URISyntaxException {
        Blob resultBlob = s3Store.get("covers/" + albumId).get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(resultBlob.getContentType()));
        headers.setContentLength(resultBlob.getInputStream().available());
        return new HttpEntity<>(IOUtils.toByteArray(resultBlob.getInputStream()), headers);
    }


}
