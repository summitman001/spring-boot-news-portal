📰 Gazze Haber Sistemi - Etkileşimli Haber Portalı



Bu proje, modern yazılım mimarileri (MVC) ve ileri seviye veritabanı programlama teknikleri kullanılarak geliştirilmiş, dinamik ve etkileşimli bir haber portalıdır. Kullanıcıların haberlerle etkileşime girebildiği (yorum, beğeni, emoji tepkileri), yöneticilerin ise arka planda detaylı analiz ve raporlama yapabildiği bir altyapıya sahiptir.



🚀 Proje Özellikleri


Kullanıcı Etkileşimi: Haberlere yorum yapabilme, favorilere ekleme, beğeni (kalp) ve çeşitli duygu emojileri (🔥 Alev, 👏 Alkış vb.) ile tepki verebilme.

Dinamik Raporlama: Yöneticiler için anlık veri üreten, özel SQL sorgularıyla desteklenmiş A4 formatında PDF/Yazdırılabilir raporlar.

Güvenlik: Spring Security ve BCrypt ile şifrelenmiş kullanıcı verileri. SQL Injection saldırılarına karşı %100 parametreli sorgu (PreparedStatement) mimarisi.



🗄️ Veritabanı Mimarisi (PostgreSQL)



Bu projenin kalbinde, sadece basit CRUD işlemlerinin ötesinde, ileri seviye PostgreSQL özellikleri yer almaktadır:



Tablo İlişkileri: Veritabanı normlarına uygun olarak `1-1` (Kullanıcı-Detay), `1-N` (Kategori-Haber) ve `M-N` (Haber-Etiket) ilişkileri kusursuz modellenmiştir.

Lookup Tables: Kategoriler gibi tekrar eden veriler için arama/referans tabloları kullanılmıştır.

Triggers (Tetikleyiciler): Veri bütünlüğünü sağlamak ve belirli işlemleri otomatize etmek için veritabanı seviyesinde tetikleyiciler yazılmıştır.

Stored Procedures (Saklı Prosedürler): Haber okunma sayılarını arttırmak gibi yoğun işlemler Java tarafında değil, performansı arttırmak amacıyla doğrudan veritabanı prosedürleriyle çözülmüştür.

Views (Görünümler): Admin paneli istatistikleri için birden fazla tablonun `JOIN` ile birleştirildiği sanal raporlama tabloları oluşturulmuştur.

Cursors (İmleçler): Satır bazlı kategori performans analizleri için PL/pgSQL kursorleri kullanılmıştır.

Aggregate Fonksiyonlar: Beğeni ve yorum sayıları parametreli `COUNT` ve `SUM` fonksiyonlarıyla anlık hesaplanmaktadır.



💻 Kullanılan Teknolojiler



Backend (Sunucu Tarafı):

Java 21

Spring Boot 3.x

Spring Data JPA (Hibernate)

Spring MVC

Spring Security



Frontend (Kullanıcı Arayüzü):

Thymeleaf (Template Engine)

HTML5, CSS3, JavaScript

Bootstrap 5 (Responsive Tasarım)

FontAwesome İkonları



Veritabanı:

PostgreSQL (pgAdmin 4)



⚙️ Kurulum ve Çalıştırma



Projeyi yerel ortamınızda çalıştırmak için aşağıdaki adımları izleyebilirsiniz:



1. Projeyi Klonlayın:

&#x20;   Terminalinize şu komutu girin:

&#x20;   `git clone https://github.com/KULLANICI\_ADIN/gazze-haber-sistemi.git`



2. Veritabanını Hazırlayın:

&#x20;   - PostgreSQL üzerinde `GazzeHaberSistemi` adında boş bir veritabanı oluşturun.

&#x20;   - Proje ana dizininde bulunan `proje\_full.sql` dosyasını çalıştırarak tüm tabloları, ilişkileri, prosedürleri ve test verilerini içeri aktarın.



3. Konfigürasyon (application.properties):

&#x20;   - `src/main/resources/application.properties` dosyasındaki veritabanı kullanıcı adı (`spring.datasource.username`) ve şifresini (`spring.datasource.password`) kendi PostgreSQL bilgilerinize göre güncelleyin.



4. Projeyi Başlatın:

&#x20;   - Bir IDE (IntelliJ, Eclipse, NetBeans) aracılığıyla veya terminal üzerinden `mvnw spring-boot:run` komutu ile uygulamayı başlatın.



5. Tarayıcıda Açın:

&#x20;   - Uygulama ayağa kalktığında tarayıcınızdan `http://localhost:8080` adresine giderek sisteme erişebilirsiniz.



Örnek Yönetici Girişi:\*

Email: admin@gmail.com

Şifre: 1234



👨‍💻 Geliştirici

Enes - Yazılım Geliştirici

Dilara Adıgüzel - Veritabanı Geliştiricisi

