package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.*;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import hk.ebsl.mfms.webservice.impl.ShunTakPOCWebServiceImpl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;


public class defectCodeTest {

    @Test
    public void test() {


        List<String> abc = new ArrayList<String>();

        Test2 tmp = new Test2();

        tmp.setXml(abc);

    }


    class Test2<T> {

        private int a;
        private int b;


        private List<T> xml;


        public int getA() {
            return a;
        }


        public void setA(int a) {
            this.a = a;
        }


        public int getB() {
            return b;
        }


        public void setB(int b) {
            this.b = b;
        }


        public List<T> getXml() {
            return xml;
        }


        public void setXml(List<T> xml) {
            this.xml = xml;
        }


    }

}
