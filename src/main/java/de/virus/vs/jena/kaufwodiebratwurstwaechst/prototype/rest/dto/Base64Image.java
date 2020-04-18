package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto;

import org.bson.internal.Base64;
import org.bson.types.Binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

public class Base64Image {
    
    private Base64Image() {
        // hide constructor
    }
    
    public static String of(Binary binary) {
        return of(binary, mediaTypeOf(binary));
    }

    public static String of(Binary binary, String mediaType) {
        if(binary == null) {
            return null;
        }
        return "data:" + mediaType + ";base64," + Base64.encode(binary.getData());
    }

    public static String mediaTypeOf(Binary image) {
        if(image == null) {
            return null;
        }
        try {
            return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image.getData()));
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }
    
    
}
