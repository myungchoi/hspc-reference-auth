package org.hspconsortium.platform.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseTokenService {

    private Log log = LogFactory.getLog(FirebaseTokenService.class);

    private FirebaseApp firebaseApp;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    private void initFirebase() {
        InputStream firebaseCredentials = null;
        try {
            firebaseCredentials = resourceLoader.getResource("classpath:firebase-key.json").getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(firebaseCredentials))
                .build();

        firebaseApp = FirebaseApp.initializeApp(options);
    }

    public FirebaseToken validateToken(String firebaseJwt){
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            Task<FirebaseToken> varifiedToken = firebaseAuth.verifyIdToken(firebaseJwt);
            Tasks.await(varifiedToken);
            return varifiedToken.getResult();
        } catch(Throwable ex) {
            log.info("Expired token value: " + firebaseJwt);
            return null;
        }
    }

    public FirebaseToken lookupUser(String username){
       return null;
    }
}
