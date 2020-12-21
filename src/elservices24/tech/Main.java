package elservices24.tech;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Map<InetAddress, Boolean> mapAddressCheck = new HashMap<>();
        List<String> resultList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String inputAddress;
        System.out.println("Введите список адресов для проверки. По окончанию ввода списка наберите в консоли exit");
        while (!(inputAddress=scanner.nextLine()).equalsIgnoreCase("exit")){
            try {
                mapAddressCheck.put(InetAddress.getByName(inputAddress), false);
            } catch (UnknownHostException e) {
                System.err.println("Адрес введен не верно. Проверьте вводимый адрес и повторите попытку");
            }
        }
        System.out.println("Проверяю список адресов. Осталось проверить: ");
        int size = mapAddressCheck.size();
        for (Map.Entry<InetAddress, Boolean> cheking:mapAddressCheck.entrySet()) {
            try {
                if (cheking.getKey().isReachable(1000)) cheking.setValue(true);
                size--;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = (size<5)?" адреса":" адресов";
            System.out.println(size + str);
        }
        System.out.println(System.lineSeparator() + "Результаты проверки:");

        for (Map.Entry<InetAddress, Boolean> cheking:mapAddressCheck.entrySet()) {
            String result = (cheking.getValue())?"доступен":"недоступен";
            result = cheking.getKey()+" "+result;
            resultList.add(result.trim()+System.lineSeparator());
            System.out.println(result);
        }

        System.out.println("Хотите сохранить результаты в файл (Yes/No)");
        while (true) {
            String yesNo = scanner.nextLine();
            if (yesNo.equalsIgnoreCase("y") || yesNo.equalsIgnoreCase("yes")) {
                System.out.println("Введите путь для сохранения (данные будут перезапиcаны)");
                File saveFile = new File(scanner.nextLine());
                if (!saveFile.exists()) {
                    try {
                        saveFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(saveFile))) {
                    for (String strResult : resultList) {
                        dataOutputStream.writeUTF(strResult);
                    }
                    System.out.println("Данные сохранены в файл "+saveFile.getAbsolutePath());
                    break;
                } catch (FileNotFoundException e) {
                    System.out.println("Путь к файлу указан не верно, повторите ввод");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else break;
        }
    }
}
