package com.alcanl.app.process.child;

public final class Common {

    public final static String ms_title = "Kısmet Oto Stok Takip Sistemi";
    public final static String ms_warningTitle = "Uyarı";
    public final static String ms_warningMessage = "Alanlar Boş Bırakılamaz.";
    public final static String ms_errorTitle = "Hata";
    public final static String ms_successAlreadyMessageFromDatabaseChild = "Database_Already_Created";
    public final static String ms_successMessageFromDatabaseChild = "Database_Created_Successfully";
    public final static String ms_errorMessageFromDatabaseChild = "Error_Occurred_On_Create_Database";
    public final static String ms_errorMessageFromMainChild = "Failed to initialize JPA EntityManagerFactory";
    public final static String ms_successMessageFromMainChild = "Successfully_Started_Main_App";
    public final static String ms_mainAppPath = System.getenv("ProgramFiles(x86)") + "\\Kısmet Oto\\bin\\Kismet-Oto-Stock-Tracking-System-1.0.0.jar";
    public final static String ms_dbStarterAppPath = System.getenv("ProgramFiles(x86)") + "\\Kısmet Oto\\bin\\Kismet-Auto-Stock-Tracking-App-Database-Starter-1.0.0.jar";
    public final static String ms_logoPath = System.getenv("ProgramFiles(x86)") + "\\Kısmet Oto\\assets\\default_logo.png";
    public final static String ms_errorMessage = "Veritabanı Bağlantı Hatası\nKullanıcı Adı / Parola Hatalı ya da Veritabanı Sunucuları Kapatılmış Olabilir";
    public final static String ms_starterDatabaseUsernamePropArgument = "--spring.datasource.username=%s";
    public final static String ms_starterDatabasePasswordPropArgument = "--spring.datasource.password=%s";
    public final static String ms_starterCommandJava = "java";
    public final static String ms_starterCommandJar = "-jar";
    private Common() {}
}
