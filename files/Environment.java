/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expedition.game_tools;

import static expedition.Other.*;
import static expedition.Frame.tab;
import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Environment {

    public enum Weather {
        Rainy,
        Thunderstorm,
        Cloudy,
        Snowing,
        Foggy,
        Clear;
    }

    public enum Biome {
        Desert,
        Woodlands,
        Grasslands,
        Tundra,
        Mountains;
    }

    public enum Temperature {
        Hot,
        Temperate,
        Cool;
    }

    private Queue<Weather> weatherQueue;
    private Weather weather;
    //private int weatherSeverity;
    private Biome biome;
    private Temperature temp;
    private Date date;

    public Environment(Date date) {
        this.date = date;
        biome = pickRandomBiome();
        temp = pickRandomTemperature();
        weatherQueue = new LinkedList<>();
        weatherQueue.add(Weather.Clear); //The first two days are always clear.
        weather = Weather.Clear;
    }

    //when a new day starts, weather is pulled from the weather queue.
    //if the weather queue is empty, this method will schedule new weather.
    public void increment(Date date) {
        this.date = date;
        date.increment();

        if (weatherQueue.size() > 1) {
            weather = weatherQueue.poll();
        } else {
            scheduleNewWeather();
            weather = weatherQueue.poll();
        }

        switchTemperature();
    }

    public void increment(Date date, int num) {
        for (int i = 0; i < num; i++) {
            increment(date);
        }
    }

    

    //adds weather to the weatherQueue based on a random WeatherEvent, which is influenced
    //by time of year and biome.
    public void scheduleNewWeather() {
        WeatherEvent event = generateWeather();
        Random rand = new Random();
        switch (event) {
            case Storm: //add 1-2 days of clouds, then snow if it is cold, or 1-2 days of rain and then a thunderstorm if it isn't.
                for (int i = 0; i < d2(4); i++) {
                    weatherQueue.add(Weather.Cloudy);
                }
                if (temp == Temperature.Cool) {
                    for (int i = 0; i < d2(4); i++) {
                        weatherQueue.add(Weather.Snowing);
                    }
                } else {
                    for (int i = 0; i < d2(4); i++) {
                        weatherQueue.add(Weather.Rainy);
                    }
                    weatherQueue.add(Weather.Thunderstorm);
                }
                break;
            case Rain: //add 1-2 days of rain or snow depending on temperature.
                if (temp == Temperature.Cool) {
                    for (int i = 0; i < d2(4); i++) {
                        weatherQueue.add(Weather.Snowing);
                    }
                } else {
                    for (int i = 0; i < d2(4); i++) {
                        weatherQueue.add(Weather.Rainy);
                    }
                }
                break;
            case Cloudy: //add 1-2 days of clouds.
                for (int i = 0; i < d2(4); i++) {
                    weatherQueue.add(Weather.Cloudy);
                }
                break;
            case Fog: //add 1-2 days of fog.
                for (int i = 0; i < d2(4); i++) {
                    weatherQueue.add(Weather.Foggy);
                }
                break;
            case Clear: //add 2-3 days of clear weather.
                for (int i = 0; i < (1 + d2(4)); i++) {
                    weatherQueue.add(Weather.Clear);
                }
                break;
        }
    }

    public void flushWeatherQueue() {
        weatherQueue.clear();
    }

    public void switchBiome() {
        Biome newBiome = pickRandomBiome();
        while ((newBiome == Biome.Desert && this.biome == Biome.Tundra) || (newBiome == Biome.Tundra && this.biome == Biome.Desert) || newBiome == this.biome) {
            newBiome = pickRandomBiome();
        }
        biome = newBiome;
        setRandomTemperature();
        flushWeatherQueue();
        scheduleNewWeather();
    }

    public void setRandomTemperature() {
        this.temp = pickRandomTemperature();
    }

    public void switchTemperature() {
        switch (temp) {
            case Hot:
                switch (d2(2)) { //33% chance to switch to a new temperature. Can only change temperature one at a time.
                    case 1:
                        temp = Temperature.Hot;
                    case 2:
                        temp = pickRandomTemperature();
                        if (temp == Temperature.Cool) {
                            temp = Temperature.Temperate;
                        }
                }
            case Temperate:
                switch (d2(2)) { //33% chance to switch to a new temperature. Can only change temperature one at a time.
                    case 1:
                        temp = Temperature.Temperate;
                    case 2:
                        temp = pickRandomTemperature();
                }
            case Cool:
                switch (d2(2)) { //33% chance to switch to a new temperature. Can only change temperature one at a time.
                    case 1:
                        temp = Temperature.Cool;
                    case 2:
                        temp = pickRandomTemperature();
                        if (temp == Temperature.Hot) {
                            temp = Temperature.Temperate;
                        }
                }
        }
    }

    //returns a random biome.
    private static Biome pickRandomBiome() {
        Map<Biome, Integer> biomePick = new HashMap<>();
        biomePick.put(Biome.Grasslands, 50);
        biomePick.put(Biome.Woodlands, 30);
        biomePick.put(Biome.Desert, 10);
        biomePick.put(Biome.Tundra, 5);
        biomePick.put(Biome.Mountains, 15);
        return pickRandom(biomePick);
    }

    //an enum of meteorological events that can cause different types of
    //precipitation (Weather)
    public enum WeatherEvent {
        Storm, Rain, Cloudy, Fog, Clear;
    }

    //generates a WeatherEvent based on time of year and biome.
    private WeatherEvent generateWeather() {
        Weather choice;
        Map<WeatherEvent, Integer> tempPick = new HashMap<>();
        tempPick.put(WeatherEvent.Storm, 5);
        tempPick.put(WeatherEvent.Rain, 10);
        tempPick.put(WeatherEvent.Cloudy, 15);
        tempPick.put(WeatherEvent.Fog, 10);
        tempPick.put(WeatherEvent.Clear, 60);

        switch (date.getMonth()) {
            case Abb:
                incMapping(tempPick, WeatherEvent.Storm, 10);
                incMapping(tempPick, WeatherEvent.Rain, 15);
                incMapping(tempPick, WeatherEvent.Fog, 5);
                break;
            case Bea:
                incMapping(tempPick, WeatherEvent.Storm, 5);
                incMapping(tempPick, WeatherEvent.Rain, 10);
                incMapping(tempPick, WeatherEvent.Clear, 25);
                break;
            case Cee:
                incMapping(tempPick, WeatherEvent.Cloudy, 15);
                incMapping(tempPick, WeatherEvent.Fog, 10);
                break;
            case Dei:
                incMapping(tempPick, WeatherEvent.Cloudy, 10);
                incMapping(tempPick, WeatherEvent.Rain, 10);
                incMapping(tempPick, WeatherEvent.Storm, 5);
                break;
        }
        switch (biome) {
            case Grasslands:
                incMapping(tempPick, WeatherEvent.Clear, 10);
                break;
            case Woodlands:
                incMapping(tempPick, WeatherEvent.Fog, 10);
                break;
            case Desert:
                tempPick.put(WeatherEvent.Rain, 0);
                tempPick.put(WeatherEvent.Storm, 0);
                tempPick.put(WeatherEvent.Fog, 0);
                incMapping(tempPick, WeatherEvent.Cloudy, -10);
                incMapping(tempPick, WeatherEvent.Clear, 50);
                break;
            case Tundra:
                incMapping(tempPick, WeatherEvent.Fog, 10);
                incMapping(tempPick, WeatherEvent.Cloudy, 10);
                break;
            case Mountains:
                incMapping(tempPick, WeatherEvent.Rain, 15);
                incMapping(tempPick, WeatherEvent.Fog, 20);
                break;
        }
        if (temp == Temperature.Hot) {
            tempPick.put(WeatherEvent.Fog, 0);
        }
        return pickRandom(tempPick);
    }

    //returns a random temperature based on time of year and biome.
    private Temperature pickRandomTemperature() {
        Map<Temperature, Integer> tempPick = new HashMap<>();
        tempPick.put(Temperature.Hot, 10);
        tempPick.put(Temperature.Temperate, 40);
        tempPick.put(Temperature.Cool, 10);
        switch (date.getMonth()) {
            case Abb:
                incMapping(tempPick, Temperature.Temperate, 30);
                incMapping(tempPick, Temperature.Cool, 40);
                break;
            case Bea:
                incMapping(tempPick, Temperature.Hot, 60);
                tempPick.put(Temperature.Cool, 0);
                break;
            case Cee:
                incMapping(tempPick, Temperature.Temperate, 15);
                incMapping(tempPick, Temperature.Hot, 5);
                break;
            case Dei:
                incMapping(tempPick, Temperature.Cool, 70);
                tempPick.put(Temperature.Hot, 0);
                break;
        }
        switch (biome) {
            case Grasslands:
                break;
            case Woodlands:
                break;
            case Desert:
                incMapping(tempPick, Temperature.Temperate, 15);
                incMapping(tempPick, Temperature.Hot, 5);
                break;
            case Tundra:
                incMapping(tempPick, Temperature.Temperate, 10);
                incMapping(tempPick, Temperature.Cool, 70);
                tempPick.put(Temperature.Hot, 0);
                break;
            case Mountains:
                incMapping(tempPick, Temperature.Temperate, 30);
                incMapping(tempPick, Temperature.Cool, 50);
                break;
        }
        return pickRandom(tempPick);
    }

    //returns the top element of WeatherQueue (without removing it)
    public Weather peekAhead() {
        return weatherQueue.peek();
    }

    //getters.
    public Weather getWeather() {
        return weather;
    }

    public Biome getBiome() {
        return biome;
    }

    public Temperature getTemp() {
        return temp;
    }

    public String descriptionOfEnvironment() {
        Map<String, Integer> tempPick = new HashMap<>();

        switch (biome) {
            case Grasslands: //GRASSLANDS////////////////////

                switch (temp) {

                    case Hot: // HOT GRASSLANDS // // //
                        switch (weather) {
                            case Rainy:
                                tempPick.put(tab + "Warm " + date.getMonth().getName() + " showers cover your travelers and the surrounding grasslands in a fine layer of wetness."
                                        + "\n" + tab + "The hot air evaporates the rain quickly, surrounding your travelers in a fine thickness of mist", 1);
                                tempPick.put(tab + "The sky is pouring rain onto your travelers and the surrounding grasslands."
                                        + "\n" + tab + "Your travelers shed their clothes in the hot air and enjoy the refreshing downpour.", 1);
                                tempPick.put(tab + "The pit-pattering of " + date.getMonth().getName() + " showers greet your sweat-covered travelers."
                                        + "\n" + tab + "They enjoy the day outside, cleansing their sweaty bodies and cleaning their belongings in the relaxing rain", 1);
                                break;
                            case Thunderstorm:
                                tempPick.put(tab + "The rain pouring from the sky cools and refreshes your weary travelers."
                                        + "\n" + tab + "Scattered and frequent thunder-claps keep your travelers from relaxing for too long.", 1);
                                break;
                            case Cloudy:
                                tempPick.put(tab + "The hot sun beaming down on the nearby grasslands is unbearable to your travelers."
                                        + "\n" + tab + "Luckily, a scattered cloud coverage blocks some of the direct sunlight and frequently allows your travelers a break from the sun's rays.", 1);
                                break;
                            case Snowing: //N/A//
                            case Foggy: //N/A//
                            case Clear:
                                tempPick.put(tab + "The dense grasslands surrounding your travelers are lit by a boiling hot sun overhead."
                                        + "\n" + tab + "The intense sun threatens to burn your travelers' skin any time they dare to leave the safety of the wagon's shady cover.", 1);
                                break;

                        }
                        break;
                    case Temperate: // TEMPERATE GRASSLANDS // // //
                        switch (weather) {
                            case Rainy:
                                tempPick.put(tab + "Light showers cover the wide expanse of grassland surrounding your travelers.", 1);
                                break;
                            case Thunderstorm:
                                tempPick.put(tab + "Scattered thunder-claps amidst dense rain keep your travelers from relaxing today.", 1);
                                break;
                            case Cloudy:
                                tempPick.put(tab + "The grasslands surrounding your travelers are shaded by sporadic cloud coverage.", 1);
                                break;
                            case Snowing:
                            case Foggy:
                                tempPick.put(tab + "Dense fog sits upon the surrounding grasslands, making it difficult to see very far in any direction.", 1);
                                break;
                            case Clear:
                                tempPick.put(tab + "The bright sun smiles down upon the grasslands your travelers find themselves on.", 1);
                                break;
                        }
                        break;
                    case Cool: // COOL GRASSLANDS // // //
                        switch (weather) {
                            case Rainy: //N/A//
                            case Thunderstorm: //N/A//
                            case Cloudy:
                                tempPick.put(tab + "White clouds billow in the sky, breathing darkness and mystery into the cold grasslands your travelers find themselves in."
                                        + "\n" + tab + "Your travelers bundle up for the cold weather and look up into the sky with a sense of dark anticipation.", 1);
                                break;
                            case Snowing:
                                tempPick.put(tab + "Trickling snowflakes tickle your travelers and blanket the surrounding grasslands with snow."
                                        + "\n" + tab + "Your travelers huddle around a fire, bundled up in all the clothing and rags they could find within the wagon.", 1);
                                break;
                            case Foggy:
                                tempPick.put(tab + "White fog lazily blows over the surrounding grasslands."
                                        + "\n" + tab + "Your travelers bundle up around a fire to avoid the freezing air that accompanies the dense mist.", 1);
                                break;
                            case Clear:
                                tempPick.put(tab + "The sun smiles down upon your travelers and the nearby grasslands they find themselves in."
                                        + "\n" + tab + "The air is freezing to the skin, so your travelers bundle up in whatever clothing and rags they can find around the wagon.", 1);
                                break;
                        }
                        break;
                }
                break;

            case Woodlands: //WOODLANDS////////////////////

                switch (temp) {

                    case Hot: // HOT WOODLANDS // // //
                        switch (weather) {
                            case Rainy:
                                tempPick.put(tab + "Cool rain falls onto the forest canopy above your weary and hot travelers."
                                        + "\n" + tab + "They clean their sweaty clothes and bodies in the downpour.", 1);
                                break;
                            case Thunderstorm:
                                tempPick.put(tab + "Thunder and heavy rain beat against the trees and sweltering air surrounding your travelers."
                                        + "\n" + tab + "The light from the storm occasionally bursts through the sporadic gaps in the forest canopy and thick cloud layer above.", 1);
                                break;
                            case Cloudy:
                                tempPick.put(tab + "Few rays of light can puncture through the forest canopy and the thick clouds billowing in the sky today."
                                        + "\n" + tab + "Your travelers, no longer needing to avoid sunlight, shed their clothes to beat the heat of the season.", 1);
                                break;
                            case Snowing://N/A//
                            case Foggy://N/A//
                            case Clear:
                                tempPick.put(tab + "The sun's rays beat down on the hot forest canopy above your sweaty travelers."
                                        + "\n" + tab + "They hide under the shade from trees to cool down.", 1);
                                break;
                        }
                        break;
                    case Temperate: // TEMPERATE WOODLANDS // // //
                        switch (weather) {
                            case Rainy:
                                tempPick.put(tab + "Rain from the sky above the forest canopy makes its way into the roots of the trees below."
                                        + "\n" + tab + "Your travelers clean their equipment and clothes in the rushing rainwater.", 1);
                                break;
                            case Thunderstorm:
                                tempPick.put(tab + "Thunder blasts down angrily from the dark clouds above."
                                        + "\n" + tab + "Your travelers find themselves hiding under the biggest trees they can find to avoid the whipping wind and rain.", 1);
                                break;
                            case Cloudy:
                                tempPick.put(tab + "Through the gaps in the forest canopy, your travelers spot dense clouds rolling across the sky."
                                        + "\n" + tab + "Your travelers enjoy imagining animals and food amongst the clouds.", 1);
                                break;
                            case Snowing:
                            case Foggy:
                                tempPick.put(tab + "Thick gobs of milky fog permeate the woodlands around your travelers."
                                        + "\n" + tab + "They have to move carefully to avoid tripping over any roots, rocks, or trunks.", 1);
                                break;
                            case Clear:
                                tempPick.put(tab + "The sound of birds whistling in the woodlands greets your travelers."
                                        + "\n" + tab + "The sun gazes down at the forest and its inhabitants below.", 1);
                                break;
                        }
                        break;
                    case Cool: // COOL WOODLANDS // // //
                        switch (weather) {
                            case Rainy: //N/A//
                            case Thunderstorm: //N/A//
                            case Cloudy:
                                tempPick.put(tab + "Frosty clouds loom over the woodlands your travelers find themselves in, and paint the sky white."
                                        + "\n" + tab + "Your travelers can't help bundling themselves up inside the wagon, away from the freezing temperatures outside.", 1);
                                break;
                            case Snowing:
                                tempPick.put(tab + "Snowflakes pour from the sky above the forest canopy, dusting the trees and whatever travelers they can find in a milky white covering."
                                        + "\n" + tab + "Your travelers bundle close around a fire and hope for the cold to pass.", 1);
                                break;
                            case Foggy:
                                tempPick.put(tab + "Thick clouds of cold air bellow around your travelers and inbetween trees."
                                        + "\n" + tab + "Your travelers tread carefully due to the cold weather and limited visibility.", 1);
                                break;
                            case Clear:
                                tempPick.put(tab + "The sun shines above the forest canopy, but isn't strong enough today to battle the cold facing your adventurers."
                                        + "\n" + tab + "The company bundles itself in rags and huddles around fires to keep warm.", 1);
                                break;
                        }
                        break;
                }
                break;

            case Desert: //DESERT////////////////////

                switch (temp) {

                    case Hot: // HOT DESERT // // //
                        switch (weather) {
                            case Rainy:
                            case Thunderstorm:
                            case Cloudy:
                                tempPick.put(tab + "Clouds overhead provide long swathes of shadow on top of the boiling hot desert sands."
                                        + "\n" + tab + "Your adventurers chase what little cover from the oppresive sun they can.", 1);
                                break;
                            case Snowing:
                            case Foggy:
                            case Clear:
                                tempPick.put(tab + "The sweltering sun beats down on the desert your travelers find themselves in."
                                        + "\n" + tab + "They hide inside the wagon where it is a few degrees cooler.", 1);
                                tempPick.put(tab + "The heat in the air is visible on the horizon of the sweltering desert."
                                        + "\n" + tab + "Your travelers cover themselves in whatever they can find to avoid the dangerous sunrays.", 1);
                                break;
                        }
                        break;
                    case Temperate: // TEMPERATE DESERT // // //
                        switch (weather) {
                            case Rainy:
                            case Thunderstorm:
                            case Cloudy:
                                tempPick.put(tab + "Clouds block some of the sun from beating down on the desert today."
                                        + "\n" + tab + "Your travelers press on through the sands, searching for water to bathe in and drink.", 1);
                                break;
                            case Snowing:
                            case Foggy:
                            case Clear:
                                tempPick.put(tab + "The sun shines brightly upon the surface of the desert."
                                        + "\n" + tab + "Your travelers look down to protect their eyes from the blinding light, and trudge onward through the endless sands.", 1);
                                break;
                        }
                        break;
                    case Cool: // COOL DESERT // // //
                        switch (weather) {
                            case Rainy:
                            case Thunderstorm:
                            case Cloudy:
                                tempPick.put(tab + "The sun is low on the desert today, and a milky cloud coverage steals heat and light from your weary travelers."
                                        + "\n" + tab + "They bundle up in clothes and press onward, eager to escape the endless sand and freezing blowing wind.", 1);
                                break;
                            case Snowing:
                            case Foggy:
                            case Clear:
                                tempPick.put(tab + "The sun is bright but cold on the desert today, even though the skies are completely clear."
                                        + "\n" + tab + "The dark air brings cold whips of wind that lash your travelers, who bundle up in clothes for protection.", 1);
                                break;
                        }
                        break;
                }
                break;

            case Mountains: //MOUNTAINS////////////////////

                switch (temp) {

                    case Hot: // HOT MOUNTAINS // // //
                        switch (weather) {
                            case Rainy:
                                tempPick.put(tab + "Hot winds bring refreshing air and light showers to your travelers as they trudge along the mountain path."
                                        + "\n" + tab + "They bathe in the clean rainwater and wash away the sweat caused by the sweltering heat.", 1);
                                break;
                            case Thunderstorm:
                                tempPick.put(tab + "Swirling dark clouds bring scattered thunderstorms, thick rain, and heavy hot winds above the mountaintops your travelers find themselves on."
                                        + "\n" + tab + "They take cover from the hot whipping winds and rain behind rocks and under the occasional tree along the rocky mountain ridge.", 1);
                                break;
                            case Cloudy:
                                tempPick.put(tab + "Ominous clouds hang in the sky above the craggy mountain path your travelers find themselves on."
                                        + "\n" + tab + "The clouds and occasional tree along the rocky ridge bring your travelers well deserved relief from the sun's sweltering heat.", 1);
                                break;
                            case Snowing:
                            case Foggy:
                            case Clear:
                                tempPick.put(tab + "The hot sun beats down upon your travelers through clear skies as they tredge along the mountain ridge.", 1);
                                break;
                        }
                        break;
                    case Temperate: // TEMPERATE MOUNTAINS // // //
                        switch (weather) {
                            case Rainy:
                                tempPick.put(tab + "Evening showers fall upon the mountain ridge today."
                                        + "\n" + tab + "Your travelers enjoy the refreshing rain as their wagon bumps along the rocky path.", 1);
                                break;
                            case Thunderstorm:
                                tempPick.put(tab + "Dark clouds bring crashing thunder, rushing rain, and powerful winds atop the mountains your travelers find themselves on."
                                        + "\n" + tab + "They hide from the weather behind the cover of large rocks and trees scattered along the rocky path.", 1);
                                break;
                            case Cloudy:
                                tempPick.put(tab + "A light cloud coverage blocks the sunlight from reaching the base of the mountains."
                                        + "\n" + tab + "Your travelers trudge along the craggy path, looking up with anticipation at the clouds swirling around the highest mountain peaks.", 1);
                                break;
                            case Snowing:
                            case Foggy:
                                tempPick.put(tab + "Swirling fog encircles your travelers on the mountain path."
                                        + "\n" + tab + "Your travelers find it hard to tell how high up they are due to the limited visibility.", 1);
                                break;
                            case Clear:
                                tempPick.put(tab + "Your travelers enjoy the lovely weather and clear skies atop the mountain path."
                                        + "\n" + tab + "The landscape is gorgeous, and your travelers enjoy looking off into the distance at the vast landscape.", 1);
                                break;
                        }
                        break;
                    case Cool: // COOL MOUNTAINS // // //
                        switch (weather) {
                            case Rainy:
                            case Thunderstorm:
                            case Cloudy:
                                tempPick.put(tab + "The sky is stained white with frosty clouds."
                                        + "\n" + tab + "Your travelers trudge along the mountain path, trying their best to stay warm.", 1);
                                break;
                            case Snowing:
                                tempPick.put(tab + "Snow pours onto the mountain path from the white sky above."
                                        + "\n" + tab + "Your travelers enjoy the chilly taste of the snowflakes.", 1);
                                break;
                            case Foggy:
                                tempPick.put(tab + "Crisp white fog encircles your travelers on the rocky mountain ridge."
                                        + "\n" + tab + "Visibility is low, and your travelers are freezing cold.", 1);
                                break;
                            case Clear:
                                tempPick.put(tab + "The sky is clear and sunny today."
                                        + "\n" + tab + "Your travelers enjoy the sunlight, even though it is freezing cold atop the mountain they find themseles on.", 1);
                                break;
                        }
                        break;
                }
                break;

            case Tundra: //TUNDRA////////////////////

                switch (temp) {

                    case Hot: // HOT TUNDRA // // // --- NOT POSSIBLE
                    case Temperate: // TEMPERATE TUNDRA // // //
                        switch (weather) {
                            case Rainy:
                                tempPick.put(tab + "Showers refresh your travelers as they camp out on the tundra.", 1);
                                break;
                            case Thunderstorm:
                                tempPick.put(tab + "Scattered lighting crackles through the sky as hard wind and heavy rain whip the surface of the tundra.", 1);
                                break;
                            case Cloudy:
                                tempPick.put(tab + "Milky clouds roll softly across the sky."
                                        + "\n" + tab + "Your travelers gaze upwards longingly.", 1);
                                break;
                            case Snowing:
                            case Foggy:
                                tempPick.put(tab + "Fog encircles your travelers as they camp on the tundra."
                                        + "\n" + tab + "It's difficult to see far in any direction.", 1);
                                break;
                            case Clear:
                                tempPick.put(tab + "The frosty tundra stretches as far as the eyes can see."
                                        + "\n" + tab + "Your travelers enjoy the pleasant weather and clear skies.", 1);
                                break;
                        }
                        break;
                    case Cool: // COOL TUNDRA // // //
                        switch (weather) {
                            case Rainy:
                            case Thunderstorm:
                            case Cloudy:
                                tempPick.put(tab + "The air is freezing on the surface of the tundra your travelers find themselves on."
                                        + "\n" + tab + "A thick layer of white clouds lulls across the arctic sky.", 1);
                                break;
                            case Snowing:
                                tempPick.put(tab + "Heavy snowfall greets your travelers as they camp on the tundra."
                                        + "\n" + tab + "The snowflakes freeze their skin, so your travelers bundle up in as many blankets as they can fit into.", 1);
                                break;
                            case Foggy:
                                tempPick.put(tab + "Thick gobs of frozen fog surround your travelers' camp."
                                        + "\n" + tab + "It's hard to go too far in this weather, so your travelers huddle around a dwindling fire to keep warm from the arctic wind.", 1);
                                break;
                            case Clear:
                                tempPick.put(tab + "The tundra air freezes your travelers down to the bone."
                                        + "\n" + tab + "Regardless, the sky is clear and the sun is bright.", 1);
                                break;
                        }
                        break;
                }
                break;

            default: //ERROR?//
                tempPick.put("ERROR in Environment.java - descriptionOfEnvironment", 1000);

        }
        return pickRandom(tempPick);

    }

}
