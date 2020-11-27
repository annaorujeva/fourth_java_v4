package app.store;


import app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreService {

    @Autowired
    Repository filerepository;

    public StoreService(){};

    public void startofwork() {
        filerepository.setDirectory("C:/Users/Anna/lr4_rv/");
        if (filerepository.isDirectoryExists()) {
            if (filerepository.isEmptyDirectory()) {
                filerepository.GenerateDir();
            }
            else {
                filerepository.read();
            }
        }
        else {
            filerepository.createDirectory();
            filerepository.GenerateDir();
        }
    }
}
