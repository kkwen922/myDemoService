package my.demo.hello;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/hello")
public class HelloController {

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value = "/{id}", method =  RequestMethod.GET)
    public String getHostname(@PathVariable Long id){

        JSONObject obj = new JSONObject();
        try{
            InetAddress addr = InetAddress.getLocalHost();
            String hostname =addr.getHostName().toString();//獲得本機名稱
            obj.put("hostname",hostname);

            InetAddress Addresses[] = InetAddress.getAllByName(hostname);
            ArrayList<java.lang.String> ip_array = new ArrayList<>();

            JSONObject ipObject = new JSONObject();

            int i=1;
            for(InetAddress address1 : Addresses){
                log.info(hostname + " ====> "+ address1.getHostAddress());
                ip_array.add(address1.getHostAddress());
                ipObject.put("add"+i,address1.getHostAddress());
                i++;
            }
            //obj.put("ip_list",ip_array);
            obj.put("ip_list",ipObject);

        }catch (Exception ex){
            log.info("ex");
        }
        log.info(obj.toString());
        log.debug(obj.toString());
        log.error(obj.toString());
        log.warn(obj.toString());
        return obj.toString();
    }

}
