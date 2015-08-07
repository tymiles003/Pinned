package com.example.sendforhelp.sendforhelp;

public class Contact
{
    String name, phone,email;

    public Contact(String name, String phone, String email)
    {
        this.name = name;
        this.phone = phone.replace("(","").replace(")","").replace("-","");
        this.email = email;
    }

}
