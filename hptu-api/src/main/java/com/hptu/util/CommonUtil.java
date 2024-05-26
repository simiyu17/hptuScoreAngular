package com.hptu.util;

import com.hptu.hptuassessment.dto.CountyDto;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author simiyu
 */
public abstract class CommonUtil implements ApplicationContextAware {

    protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    public static List<CountyDto> getKenyanCounties(){
        List<CountyDto> countyDtos = new ArrayList<>();
        getKenyanCountiesMap().forEach((key1, value) -> countyDtos.add(new CountyDto(key1, value)));
        countyDtos.sort(Comparator.comparing(CountyDto::name));
        return countyDtos;
    }

    public static String getCountyByCode(String code){
        return getKenyanCountiesMap().get(code);
    }

    public static Map<String, String> getKenyanCountiesMap(){
        Map<String, String> map = new HashMap<>();
        map.put("001", "Mombasa");
        map.put("002", "Kwale");
        map.put("003", "Kilifi");
        map.put("004", "Tana River");
        map.put("005", "Lamu");
        map.put("006", "Taita–Taveta");
        map.put("007", "Garissa");
        map.put("008", "Wajir");
        map.put("009", "Mandera");
        map.put("010", "Marsabit");
        map.put("011", "Isiolo");
        map.put("012", "Meru");
        map.put("013", "Tharaka-Nithi");
        map.put("014", "Embu");
        map.put("015", "Kitui");
        map.put("016", "Machakos");
        map.put("017", "Makueni");
        map.put("018", "Nyandarua");
        map.put("019", "Nyeri");
        map.put("020", "Kirinyaga");
        map.put("021", "Murang\'a");
        map.put("022", "Kiambu");
        map.put("023", "Turkana");
        map.put("024", "West Pokot");
        map.put("025", "Samburu");
        map.put("026", "Trans-Nzoia");
        map.put("027", "Uasin Gishu");
        map.put("028", "Elgeyo-Marakwet");
        map.put("029", "Nandi");
        map.put("030", "Baringo");
        map.put("031", "Laikipia");
        map.put("032", "Nakuru");
        map.put("033", "Narok");
        map.put("034", "Kajiado");
        map.put("035", "Kericho");
        map.put("036", "Bomet");
        map.put("037", "Kakamega");
        map.put("038", "Vihiga");
        map.put("039", "Bungoma");
        map.put("040", "Busia");
        map.put("041", "Siaya");
        map.put("042", "Kisumu");
        map.put("043", "Homa Bay");
        map.put("044", "Migori");
        map.put("045", "Kisii");
        map.put("046", "Nyamira");
        map.put("047", "Nairobi");
        return map;
    }

}
