Inventory Management System (JDBC Architecture)
Bu proje, Java'da JDBC (Java Database Connectivity) kullanarak katmanlı mimari (Layered Architecture) ve manuel transaction yönetimi mantığını kavramak amacıyla geliştirilmiş bir İş Simülasyonu çalışmasıdır.

🚀 Projenin Amacı
Modern framework'ler (Spring Boot, Hibernate/JPA) arka planda veritabanı işlemlerini nasıl yönettiğini anlamak için; bağlantı yönetiminden, SQL sorgularının çalıştırılmasına ve veri eşleşmesine (mapping) kadar tüm süreçler manuel olarak kurgulanmıştır.

🛠 Kullanılan Teknolojiler
Java 21

PostgreSQL

JDBC Driver

Maven (Bağımlılık yönetimi için)

🏗 Mimari Yapı (MVC Benzeri)
Proje, sorumlulukların ayrılması (Separation of Concerns) prensibiyle 5 ana katmana ayrılmıştır:

Config (db): Veritabanı bağlantı bilgilerinin ve Connection yönetiminin sağlandığı katman.

Entity (model): Veritabanındaki tabloların Java'daki karşılıkları (Product, Order).

Repository (dao): SQL sorgularının bulunduğu ve sadece veritabanı erişiminden sorumlu katman.

Service (business): İş mantığının (Business Logic) yürüdüğü ve Transaction (Commit/Rollback) yönetiminin yapıldığı katman.

Presentation (App): Sistemin test edildiği ve kullanıcı etkileşiminin simüle edildiği ana giriş noktası.

💡 Öne Çıkan Özellikler
Manuel Transaction Yönetimi: Sipariş verme ve stok düşürme işlemleri Connection.setAutoCommit(false) ile atomik hale getirilmiş, hata anında rollback mekanizması işletilmiştir.

Fail-Fast Yaklaşımı: Veritabanına gitmeden önce Java tarafında stok kontrolleri yapılarak gereksiz trafik önlenmiştir.

JDBC Best Practices: * SQL Injection koruması için PreparedStatement kullanımı.

Kaynak sızıntısını önlemek için try-with-resources yapısı.

Veritabanı tarafından üretilen ID'lerin (Generated Keys) geri alınması.

📝 Kurulum
PostgreSQL üzerinde inventoryDb adında bir veritabanı oluşturun.

Aşağıdaki tabloları oluşturun:

SQL

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INTEGER NOT NULL
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    product_id INTEGER REFERENCES products(id),
    quantity INTEGER NOT NULL
);
DatabaseConfig.java dosyasındaki kullanıcı adı ve şifre bilgilerini güncelleyin.

Projeyi çalıştırın.

👨‍💻 Öğrendiklerim
Bu çalışma boyunca; bir ResultSet nesnesinin nasıl map'lendiğini, veritabanı bağlantılarının neden manuel olarak kapatılması gerektiğini ve birden fazla repository işleminin neden aynı Connection üzerinden yürümesi gerektiğini (Transaction Integrity) bizzat kodlayarak deneyimledim.
