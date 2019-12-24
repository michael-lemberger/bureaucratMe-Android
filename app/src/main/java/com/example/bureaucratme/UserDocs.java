package com.example.bureaucratme;

import java.io.IOException;
/*
במחלקה הזאת צריך לממש את הקריאה מהדאטה בייס אל תוך משתמש
 */

public class UserDocs {
    private String docId;
    private String userId;

    public UserDocs(String docId, String userId){
        this.docId = docId;
        this.userId = userId;
    }

    public String getDocId() {
        return docId;
    }

    public String getUserId() {
        return userId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /*This method gets docUrl and userId(UID in the firebase auth.
     read the appropriate data from firebase database and send it to Document object wich call the fill method.
     */
    public void fillForm(){{
        /*
        כאן צריך לשלוף משתמש ומסמך מהדאטה בייס ולשלוח לאובייקט המסמך
         */
        Users user = new Users(userId);
        String docUrl = "\"https://account.gov.il/sspr/public/newuser?forwardURL=https%3A%2F%2Fmy.gov.il%2Fsec&locale=iw&locale=he\"";
        Document doc = new Document(docUrl);
        try {
            doc.fill(user);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }
}
