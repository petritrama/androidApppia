package aero.project.petritrama.prishtinainternationalairportademjashari;

import java.util.List;

public class Rent
{
    String tel1;
    String tel2;
    String tel3;
    String tel4;
    String email;
    String web;
    String schedule;
    int logo;

    private List<Rent> rents;

    Rent(String mTel1, String mTel2, String mTel3, String mTel4, String mEmail, String mWeb, String mSchedule, int mLogo)
    {
        tel1 = mTel1;
        tel2 = mTel2;
        tel3 = mTel3;
        tel4 = mTel4;
        email = mEmail;
        web = mWeb;
        schedule = mSchedule;
        logo = mLogo;
    }
}
