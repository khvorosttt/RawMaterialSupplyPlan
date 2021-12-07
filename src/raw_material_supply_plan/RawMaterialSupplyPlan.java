/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raw_material_supply_plan;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public class RawMaterialSupplyPlan {

    static int N;//количество месяцев
    static int k;//число, которому кратны партии пополнения запасов
    static int M;//ограничение на вместительность склада
    static int[] NeedsByMonth;//потребности по месяцам
    static HashMap<Integer, Integer> ReplenishmentCosts;//затраты на пополнение
    static HashMap<Integer, Integer> StorageCosts;//затраты на хранение
    static OptimalDelivery[][] optimalDelivery;
    static int minimum;//минимальные расходы
    static int stok_first;//начальный запас
    static int stok;//запас
    static List<Month> result;//запас, пополнение в месяце

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        IN();
        ReverseStep();
        //showOptimalDelivery(optimalDelivery);
        Minimum(optimalDelivery[N - 1]);
        System.out.println("Запас: "+stok + ". Минимальные расходы: " + minimum+".");
        result = new ArrayList<>();
        for (int i = 0; i<N; i++) {
            int add=DefenitionOfReplenishment(stok, optimalDelivery[N - 1 - i])>0?DefenitionOfReplenishment(stok, optimalDelivery[N - 1 - i]):0;
            result.add(new Month(stok,add));
            //System.out.println(stok +" "+result.get(i).add());
            stok =stok-NeedsByMonth[i]+add;
        }
        showAnswer(result);
    }

    public static void showAnswer(List<Month> result){
        System.out.println("Суммарные затраты предприятия на пополнение и"
                + " хранение запаса сырья будут минимальными и составят "+minimum+" ден.ед, если");
        int i=1;
        for (Month m:result) {
            System.out.println("В "+i+"-ом месяце будет приобретено "+m.add()+" ед. сырья");
            i++;
        }
        System.out.println("Запас сырья на начало 1-го месяца составляет "+stok_first+" ед.");
    }
    
    //Определение оптимального пополнения
    public static int DefenitionOfReplenishment(int i, OptimalDelivery[] optimalDelivery) {
        for (int j = 0; j < optimalDelivery.length; j++) {
            if (i == optimalDelivery[j].I()) {
                return optimalDelivery[j].X();
            }
        }
        return -1;
    }

    public static void IN() throws FileNotFoundException, IOException {
        BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + "\\data.txt")));
        String[] data_info = fin.readLine().split(" ");//плановый период, остаточная стоимость оборудования, цена единицы нового оборудования
        N = Integer.parseInt(data_info[0]);
        k = Integer.parseInt(data_info[1]);
        M = Integer.parseInt(data_info[2]);
        NeedsByMonth = new int[N];
        String[] data = fin.readLine().split(" ");
        for (int i = 0; i < NeedsByMonth.length; i++) {
            NeedsByMonth[i] = Integer.parseInt(data[i]);
        }
        ReplenishmentCosts = new HashMap<>();
        StorageCosts = new HashMap<>();
        data = fin.readLine().split(" ");
        String[] data_2 = fin.readLine().split(" ");
        for (int i = 0; i < data.length; i++) {
            ReplenishmentCosts.put(Integer.parseInt(data[i]), Integer.parseInt(data_2[i]));
        }
        data = fin.readLine().split(" ");
        data_2 = fin.readLine().split(" ");
        for (int i = 0; i < data.length; i++) {
            StorageCosts.put(Integer.parseInt(data[i]), Integer.parseInt(data_2[i]));
        }
    }

    public static int Find(OptimalDelivery[] optimalDelivery, int elem) {
        for (int i = 0; i < optimalDelivery.length; i++) {
            int d = optimalDelivery[i].I();
            if (optimalDelivery[i].I() == elem) {
                return optimalDelivery[i].F();
            }
        }
        return -1;
    }

    public static void ReverseStep() {
        optimalDelivery = new OptimalDelivery[N][];
        int min = Math.min(NeedsByMonth[N - 1], M);
        optimalDelivery[0] = new OptimalDelivery[min / k + 1];
        int i = 0;
        for (int j = 0; j < min / k + 1; j++) {
            int x = NeedsByMonth[N - 1] - i;
            int f = ReplenishmentCosts.get(x) + StorageCosts.get(NeedsByMonth[N - 1] / 2);
            optimalDelivery[0][j] = new OptimalDelivery(i, x, f);
            i += k;
        }
        int min_local = NeedsByMonth[N - 1];
        for (int l = N - 2; l >= 0; l--) {
            i = 0;
            min_local += NeedsByMonth[l];
            min = Math.min(min_local, M);
            optimalDelivery[N - 1 - l] = new OptimalDelivery[min / k + 1];
            for (int j = 0; j < min / k + 1; j++) {
                int x = 0;
                int F_result = Integer.MAX_VALUE, x_result;
                x_result = x;
                for (int s = 0; s < min / k + 1; s++) {
                    if (x >= NeedsByMonth[l] - i && x <= (min_local - i)) {
                        int F_temp = ReplenishmentCosts.get(x)
                                + StorageCosts.get(NeedsByMonth[l] / 2 + i + x - NeedsByMonth[l])
                                + Find(optimalDelivery[N - 2 - l], i + x - NeedsByMonth[l]);
                        if (F_result > F_temp) {
                            F_result = F_temp;
                            x_result = x;
                        }
                    }
                    x += k;
                }
                optimalDelivery[N - 1 - l][j] = new OptimalDelivery(i, x_result, F_result);
                i += k;
            }
        }
    }

    public static void showOptimalDelivery(OptimalDelivery[][] optimalDelivery) {
        for (int i = 0; i < optimalDelivery.length; i++) {
            for (int j = 0; j < optimalDelivery[i].length; j++) {
                OptimalDelivery temp = optimalDelivery[i][j];
                System.out.println(temp.I() + " " + temp.X() + " " + temp.F());
            }
            System.out.println("");
            System.out.println("");
        }
    }

    public static void Minimum(OptimalDelivery[] optimalDelivery) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < optimalDelivery.length; i++) {
            if (min > optimalDelivery[i].F()) {
                min = optimalDelivery[i].F();
                stok = optimalDelivery[i].I();
            }
        }
        minimum = min;
        stok_first=stok;
    }
}
