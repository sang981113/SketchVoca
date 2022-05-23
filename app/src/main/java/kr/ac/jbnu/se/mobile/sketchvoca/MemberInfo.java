package kr.ac.jbnu.se.mobile.sketchvoca;

public class MemberInfo {

    private String photoUri;
    private  String name;
    private  String phoneNumber;
    private  String birthday;

    public MemberInfo(){}

    public MemberInfo(String name, String phoneNumber, String birthday){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }
    public MemberInfo(String name, String phoneNumber, String birthday, String photoUri){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.photoUri = photoUri;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday(){
        return this.birthday;
    }
    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getPhotoUri(){ return this.photoUri; }
    public void setPhotoUri(String photoUri){ this.photoUri = photoUri; }
}
