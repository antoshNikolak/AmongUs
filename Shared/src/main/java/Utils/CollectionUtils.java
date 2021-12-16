package Utils;

import Position.Pos;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtils {
    //todo document collection utils

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Pos indexOf(Object[][] array, Object target) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (array[y][x] == target) {
                    return new Pos(x, y);
                }
            }
        }
        return null;
    }

    public static List<Pos> allIndexesOf(Object[][] array, Object target) {
        List<Pos> posList = new ArrayList<>();
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                if (array[y][x] == target) {
                    posList.add(new Pos(x, y));
                }
            }
        }
        return posList;
    }

    public static Set<Integer> findDuplicates(List<Integer> listContainingDuplicates) {
        final Set<Integer> duplicatesToReturn = new HashSet<>();
        final Set<Integer> set1 = new HashSet<>();
        for (Integer yourInt : listContainingDuplicates) {
            if (!set1.add(yourInt)) {
                duplicatesToReturn.add(yourInt);
            }
        }
        return duplicatesToReturn;
    }



    public static <T> List<T> getArrayList(T[][] matrix) {
        return Arrays.stream(matrix)  //'array' is two-dimensional
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

//    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
//        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
//        list.sort(Map.Entry.comparingByValue());
//        Map<K, V> result = new LinkedHashMap<>();
//        for (Map.Entry<K, V> entry : list) {
//            result.put(entry.getKey(), entry.getValue());
//        }
//        return result;
//    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


}
