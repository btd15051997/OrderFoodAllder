package company.com.allder1.model;

import android.content.Intent;

public class ItemAccount {
    Intent intent;
    int image ;
    String title;

    public ItemAccount(Intent intent, int image, String title) {
        this.intent = intent;
        this.image = image;
        this.title = title;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
