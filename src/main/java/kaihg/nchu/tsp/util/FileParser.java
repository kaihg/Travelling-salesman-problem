package kaihg.nchu.tsp.util;

import com.google.gson.Gson;
import kaihg.nchu.tsp.vo.City;
import kaihg.nchu.tsp.vo.Config;
import kaihg.nchu.tsp.vo.GAConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileParser {

    public static City[] parseCityFromFile(String path) throws IOException, URISyntaxException {
        ClassLoader classLoader = FileParser.class.getClassLoader();
        Stream<City> cityStream = Files.lines(Paths.get(classLoader.getResource(path).toURI())).map(s -> {
            String[] slices = s.split(" ");
            City city = new City();
            city.cityName = slices[0];
            city.x = Double.parseDouble(slices[1]);
            city.y = Double.parseDouble(slices[2]);
            return city;
        });


        return cityStream.toArray(City[]::new);
    }

    public static Config parseConfigFromFile(String path) throws FileNotFoundException {
        ClassLoader classLoader = FileParser.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        Config config = new Gson().fromJson(new FileReader(file), Config.class);
        return config;
    }

    public static GAConfig parseGAConfigFromFile(String path) throws FileNotFoundException {
        ClassLoader classLoader = FileParser.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        return new Gson().fromJson(new FileReader(file), GAConfig.class);
    }
}
