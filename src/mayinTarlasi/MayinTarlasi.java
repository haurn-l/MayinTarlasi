package mayinTarlasi;

import java.util.Random;
import java.util.Scanner;

public class MayinTarlasi {
    private String[][] harita;  // Oyun haritası
    private String[][] gorunenHarita; // Kullanıcıya gösterilen harita
    private int satirSayisi; // Satır sayısı
    private int sutunSayisi; // Sütun sayısı
    private int mayinSayisi; // Mayın sayısı
    private int acilanHucresayisi; // Açılan hücre sayısı
    private final Scanner scanner;

    public MayinTarlasi() {
        this.scanner = new Scanner(System.in);
    }

    public void oyunuBaslat() {
        System.out.println("Mayın Tarlası Oyununa Hoşgeldiniz!");

        // Kullanıcıdan oyun boyutunu al
        boyutAl();

        // Mayınları yerleştir
        mayinlariYerlestir();

        // Kullanıcıya gösterilecek haritayı oluştur
        gorunenHaritayiBaslat();

        // Oyunu başlat
        oyunuOyna();
    }

    private void boyutAl() {
        do {
            System.out.print("Satır sayısını girin (en az 2): ");
            satirSayisi = scanner.nextInt();
            System.out.print("Sütun sayısını girin (en az 2): ");
            sutunSayisi = scanner.nextInt();

            if (satirSayisi < 2 || sutunSayisi < 2) {
                System.out.println("Satır ve sütun sayısı 2'den küçük olamaz. Otomatik olarak 2x2 olarak ayarlanıyor.");
                satirSayisi = 2;
                sutunSayisi = 2;
            }
        } while (satirSayisi < 2 || sutunSayisi < 2);

        // Eleman sayısını ve mayın sayısını hesapla
        int toplamHucresayisi = satirSayisi * sutunSayisi;
        mayinSayisi = toplamHucresayisi / 4; // Çeyreği kadar mayın yerleştirilecek
        acilanHucresayisi = 0;
    }

    private void mayinlariYerlestir() {
        // Mayınları rastgele yerleştirecek metod
        harita = new String[satirSayisi][sutunSayisi];
        for (int i = 0; i < satirSayisi; i++) {
            for (int j = 0; j < sutunSayisi; j++) {
                harita[i][j] = "-";
            }
        }

        Random rand = new Random();
        int mayinlarYerlestirildi = 0;
        while (mayinlarYerlestirildi < mayinSayisi) {
            int satir = rand.nextInt(satirSayisi);
            System.out.print(satir);
            System.out.println();//bu satırlar mayının koordinatını veriyor
            int sutun = rand.nextInt(sutunSayisi);
            System.out.print(sutun);//bu satırlar mayının koordinatını veriyor
            if (!harita[satir][sutun].equals("*")) {
                harita[satir][sutun] = "*";
                mayinlarYerlestirildi++;
            }
        }
    }

    private void gorunenHaritayiBaslat() {
        gorunenHarita = new String[satirSayisi][sutunSayisi];
        for (int i = 0; i < satirSayisi; i++) {
            for (int j = 0; j < sutunSayisi; j++) {
                gorunenHarita[i][j] = "-";
            }
        }
    }

    private void oyunuOyna() {
        while (acilanHucresayisi < (satirSayisi * sutunSayisi - mayinSayisi)) {
            haritayiYazdir();
            System.out.println("1. Mayın Koordinatını Tahmin Et");
            System.out.println("2. Temiz Alan Seç");
            System.out.print("Seçiminizi yapın (1 veya 2): ");
            int secim = scanner.nextInt();

            if (secim == 1) {
                mayinTahminiYap();
            } else if (secim == 2) {
                temizAlaniSec();
            } else {
                System.out.println("Geçersiz seçim! Lütfen 1 veya 2'yi seçin.");
            }

            if (acilanHucresayisi == satirSayisi * sutunSayisi - mayinSayisi) {
                System.out.println("🌟🌟🌟Tebrikler! Tüm hücreleri açtınız ve kazandınız🌟🌟🌟.");
                break;
            }
        }
    }

    private void mayinTahminiYap() {
        System.out.print("Mayın koordinatını girin (satır ve sütun, örn: 1 2): ");
        int satir = scanner.nextInt() - 1;
        int sutun = scanner.nextInt() - 1;

        if (satir < 0 || satir >= satirSayisi || sutun < 0 || sutun >= sutunSayisi) {
            System.out.println("Geçersiz koordinat. Lütfen geçerli bir koordinat girin.");
            return;
        }

        // Mayın doğru tahmin edilirse
        if (harita[satir][sutun].equals("*")) {
            gorunenHarita[satir][sutun] = "🌟"; // Doğru tahmin, yıldız ekle
            System.out.println("Doğru tahmin! Mayını buldunuz!");
        } else {
            System.out.println("Yanlış tahmin! Mayın burada değil.\uD83D\uDCA5");
            System.out.println("Oyun bitti.");
            // Oyun bitiriliyor
            System.exit(0); // Oyun burada sonlanır
        }
    }


    private void temizAlaniSec() {
        System.out.print("Temiz alan seçin (satır ve sütun, örn: 1 2): ");
        int satir = scanner.nextInt() - 1;
        int sutun = scanner.nextInt() - 1;

        if (satir < 0 || satir >= satirSayisi || sutun < 0 || sutun >= sutunSayisi) {
            System.out.println("Geçersiz koordinat. Lütfen geçerli bir koordinat girin.");
            return;
        }
        hucreyiAc(satir, sutun);
    }

    private void hucreyiAc(int satir, int sutun) {
        if (!gorunenHarita[satir][sutun].equals("-")) {
            System.out.println("Bu hücre zaten açılmış.");
            return;
        }

        gorunenHarita[satir][sutun] = "O";
        acilanHucresayisi++;

        int etrafindakiMayinlar = etrafindakiMayinlariSay(satir, sutun);
        if (etrafindakiMayinlar > 0) {
            gorunenHarita[satir][sutun] = String.valueOf(etrafindakiMayinlar);
        }
    }

    private int etrafindakiMayinlariSay(int satir, int sutun) {
        int mayinlar = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int yeniSatir = satir + i;
                int yeniSutun = sutun + j;
                if (yeniSatir >= 0 && yeniSatir < satirSayisi && yeniSutun >= 0 && yeniSutun < sutunSayisi) {
                    if (harita[yeniSatir][yeniSutun].equals("*")) {
                        mayinlar++;
                    }
                }
            }
        }
        return mayinlar;
    }

    private void haritayiYazdir() {
        System.out.println("Oyun Haritası:");
        for (int i = 0; i < satirSayisi; i++) {
            for (int j = 0; j < sutunSayisi; j++) {
                System.out.print(gorunenHarita[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        MayinTarlasi oyun = new MayinTarlasi();
        oyun.oyunuBaslat();
    }
}
