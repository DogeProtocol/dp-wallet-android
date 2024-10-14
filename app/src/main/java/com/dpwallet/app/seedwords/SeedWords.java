package com.dpwallet.app.seedwords;

import android.content.Context;
import androidx.annotation.RawRes;
import com.dpwallet.app.R;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.utils.GlobalMethods;

public class SeedWords {
    private static  int SEED_LENGTH = 96;
    private static  Map<String, String> SEED_MAP = new HashMap<>(); //key is word, items are string corresponding to index1 and index2
    private static  Map<String, String> SEED_REVERSE_MAP = new HashMap<>(); //vice-versa of SEED_MAP
    private static  String SEED_HASH = "9289cec415e1d9db8712174987014ebbf3fff570add97793fe400aeb53740757";
    private static  String[] SEED_FRIENDLY_INDEX_ARRAY = {"a1", "a2", "a3", "a4", "b1", "b2", "b3", "b4", "c1", "c2", "c3", "c4", "d1", "d2", "d3", "d4", "e1", "e2", "e3", "e4", "f1", "f2", "f3", "f4", "g1", "g2", "g3", "g4", "h1", "h2", "h3", "h4", "i1", "i2", "i3", "i4", "j1", "j2", "j3", "j4", "k1", "k2", "k3", "k4", "l1", "l2", "l3", "l4"};
    private static ArrayList<int[]> SEED_FRIENDLY_INDEX_REVERSE_ARRAY = new ArrayList<int[]>();
    private static boolean SEED_INITIALIZED = false;
    private static  ArrayList<String> SEED_WORD_LIST = new ArrayList<String>();

    public SeedWords() {

    }

    public String sha256digestMessage(String msg) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(msg.getBytes());
            byte[] byteData = digest.digest();
            for (byte x : byteData) {
                String str = Integer.toHexString(Byte.toUnsignedInt(x));
                if (str.length() < 2) {
                    sb.append('0');
                }
                sb.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getSeedKey(String byte1, String byte2)  {
        return byte1 + "-" + byte2;
    }

    public String[]  getSeedByteIndicesFromString(String indiceKey) {
        String[] result = indiceKey.split("-");
        if (result.length == 2) {
            return result;
        }
        return null;
    }

/*
    public void initializeSeedWordsFromJson(Context context){
        @RawRes int seed = R.raw.seed;
        String seedJsonString = GlobalMethods.readRawResource(context, seed);
        PrefConnect.writeString(context,"seed",   seedJsonString);

        @RawRes int seedRevers = R.raw.seed_revers;
        String seedReversJsonString = GlobalMethods.readRawResource(context, seedRevers);
        PrefConnect.writeString(context,"seedRevers",   seedReversJsonString);

        @RawRes int seedWordList = R.raw.seed_word_list;
        String seedWordListString = GlobalMethods.readRawResource(context, seedWordList);
        PrefConnect.writeString(context,"seedWordList",   seedWordListString);

        SEED_MAP = PrefConnect.loadHashMap(context,"seed");
        SEED_REVERSE_MAP = PrefConnect.loadHashMap(context,"seedRevers");
        SEED_WORD_LIST = PrefConnect.loadArrayMap(context,"seedWordList");

        SEED_INITIALIZED = true;
    }
*/

    public boolean initializeSeedWordsFromUrl(Context context) {
        @RawRes int res = R.raw.seedwords;
        InputStream inputStream = context.getResources().openRawResource(res);
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String seedWordsRaw = s.hasNext() ? s.next() : "";
        return initializeSeedWordsFromString(context, seedWordsRaw);
    }

    public boolean initializeSeedWordsFromString(Context context, String seedWordsRaw) {

        String filedata = seedWordsRaw;
        String seedMapHashMessage = "";

        String[] lines = filedata.split("\r\n");
        if(lines.length == 0)
        {
            lines = filedata.split("\n");
        }

        for(int i=0; i<lines.length; i++) {
            String[] columns = lines[i].split(",");
            if (columns.length != 3) {
                continue;
            }

            String key = columns[0]; //word
            String left = columns[1];
            String right = columns[2];

            if (Character.codePointAt(right, 0) == 13) {
                right = columns[2].substring(0, columns[2].length() - 1);
            }
            String val = getSeedKey(left, right); //indices
            seedMapHashMessage = seedMapHashMessage + key + "=" + val + ",";

            SEED_MAP.put(key, val);
            SEED_REVERSE_MAP.put(val, key);
            SEED_WORD_LIST.add(key);
        }

        String seedhashstr = sha256digestMessage(seedMapHashMessage);
        if (seedhashstr.length() <= 0) {
            return false;
        }

        if (seedhashstr.equalsIgnoreCase(SEED_HASH)) {
        } else {
            return false;
        }

        //verify
        for (int i = 0; i <= 255; i++) {
            for (int j = 0; j <= 255; j++) {
                String testKey = getSeedKey(String.valueOf(i), String.valueOf(j));
                if (SEED_REVERSE_MAP.get(testKey).isEmpty() || SEED_REVERSE_MAP.get(testKey) == null) {
                    return false;
                }
            }
        }

        //Load Friendly Array
        SEED_FRIENDLY_INDEX_REVERSE_ARRAY = new ArrayList(SEED_LENGTH / 2);
        int count = 0;
        for (int i = 0; i < SEED_LENGTH / 2; i++) {
            int[] seedInt = new int[2];
            seedInt[0]=count;
            seedInt[1]=count + 1;
            SEED_FRIENDLY_INDEX_REVERSE_ARRAY.add(seedInt);
            count = count + 2;
        }

        SEED_INITIALIZED = true;

        return true;
    }

    public  ArrayList<String> getAllSeedWords() {
        return SEED_WORD_LIST;
    }

    public String getSeedWordFromNumberPair(int num1, int num2) {
        if (num1 < 0 || num1 > 255 || num2 < 0 || num2 > 255) {
            throw new Error("num2 numbers out of range");
        }
        String key = getSeedKey(String.valueOf(num1), String.valueOf(num2));
        return SEED_REVERSE_MAP.get(key);
    }

    public String[] getWordListFromSeedArray(String[] seedArray) {
        if (SEED_INITIALIZED == false) {
            return null;
        }

        if (seedArray.length < 2) {
            return null;
        }
        if (seedArray.length % 2 != 0) {
            return null;
        }

        String[] seedWordArray = new String[seedArray.length / 2];
        int wordIndex = 0;
        for (int i = 0; i < seedArray.length; i = i + 2) {
            String key = getSeedKey(seedArray[i], seedArray[i + 1]);
            if (SEED_REVERSE_MAP.get(key).isEmpty() || SEED_REVERSE_MAP.get(key) == null) {
                return null;
            }
            seedWordArray[wordIndex] = SEED_REVERSE_MAP.get(key);
            wordIndex = wordIndex + 1;
        }

        return seedWordArray;
    }

    public String getFriendlySeedIndex(int index) {
        if (SEED_INITIALIZED == false) {
            return null;
        }
        if (index < 0 || index > (SEED_LENGTH / 2) - 1) {
            return null;
        }
        return SEED_FRIENDLY_INDEX_ARRAY[index];
    }

    public String[] getIndicesFromFriendlySeed(String word) {
        if (SEED_INITIALIZED == false) {
            return null;
        }
        word = word.toLowerCase();
        if (SEED_MAP.get(word).isEmpty() || SEED_MAP.get(word) == null) {
            return null;
        }
        String byteIndicesString = SEED_MAP.get(word);
        String temp[] = getSeedByteIndicesFromString(byteIndicesString);
        if (temp == null) {
            return null;
        }
        String[] byteArray = new String[2];
        byteArray[0] =temp[0];
        byteArray[1] = temp[1];
        return byteArray;
    }

    public String[] getSeedArrayFromSeedWordList(String[] wordList) {
        if (SEED_INITIALIZED == false) {
            return null;
        }

        String[] seedIndexArray = new String[wordList.length * 2];
        int seedIndex = 0;
        for (int i = 0; i < wordList.length; i = i + 1) {
            String[] byteArray = getIndicesFromFriendlySeed(wordList[i]);
            if (byteArray == null) {
                return null;
            }
            seedIndexArray[seedIndex] = byteArray[0];
            seedIndexArray[seedIndex + 1] = byteArray[1];
            seedIndex = seedIndex + 2;
        }

        return seedIndexArray;
    }

    public String getWordFromFriendlySeed(int friendlySeedIndex, int[] friendlySeedArray) {
        if (SEED_INITIALIZED == false) {
            return null;
        }
        if (friendlySeedIndex < 0 || friendlySeedIndex > (SEED_LENGTH / 2) - 1) { //0 to 39 is valid range
            return null;
        }

        int actualSeedValue1 = friendlySeedArray[SEED_FRIENDLY_INDEX_REVERSE_ARRAY.get(friendlySeedIndex)[0]];
        int  actualSeedValue2 = friendlySeedArray[SEED_FRIENDLY_INDEX_REVERSE_ARRAY.get(friendlySeedIndex)[1]];
        String seedKey = getSeedKey(String.valueOf(actualSeedValue1), String.valueOf(actualSeedValue2));
        if (SEED_REVERSE_MAP.get(seedKey).isEmpty() || SEED_REVERSE_MAP.get(seedKey) == null) {
            return null;
        }
        return SEED_REVERSE_MAP.get(seedKey);
    }

    public boolean doesSeedWordExist(String word) {
        if (SEED_INITIALIZED == false) {
            return false;
        }
        word = word.toLowerCase();
        if (SEED_MAP.get(word).isEmpty() ||  SEED_MAP.get(word) == null) {
            return false;
        }

        return true;
    }

    public boolean verifySeedWord(int friendlySeedIndex, String seedWord, int[] seedArray) {
        if (SEED_INITIALIZED == false) {
            return false;
        }
        seedWord = seedWord.toLowerCase();
        if (SEED_MAP.get(seedWord).isEmpty() ||  SEED_MAP.get(seedWord) == null) {
            return false;
        }

        String actualSeedWord = getWordFromFriendlySeed(friendlySeedIndex, seedArray);
        if (actualSeedWord == null) {
            return false;
        }

        if (seedWord == actualSeedWord) {
            return true;
        }

        return false;
    }
}
