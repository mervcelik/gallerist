# Gallerist

Araç galerisi yönetimi için Spring Boot ile yazılmış bir REST API. Galericiler, araçlar, müşteriler, adresler ve banka hesapları üzerinde CRUD işlemleri yapar. Uç noktalar JWT ile korunur.

## Teknolojiler

- Java 17
- Spring Boot 4.0.8 (Web MVC, Data JPA, Security, Validation)
- PostgreSQL
- JWT (jjwt 0.11.5)
- Lombok
- springdoc-openapi (Swagger UI)
- Maven

## Kurulum

### 1. Veritabanı

PostgreSQL'de uygulamanın kullandığı şemayı oluşturun:

```sql
CREATE SCHEMA gallerist;
```

Tablolar uygulama ilk açıldığında otomatik oluşturulur (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Ayarlar

Bağlantı bilgileri `src/main/resources/application.properties` içinde:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.jpa.properties.hibernate.default_schema=gallerist
spring.datasource.username=postgres
spring.datasource.password=...
```

Kendi veritabanınıza göre düzenleyin.

### 3. Çalıştırma

```bash
./mvnw spring-boot:run
```

Uygulama `http://localhost:8080` adresinde açılır. Swagger arayüzü: `http://localhost:8080/swagger-ui.html`

## Kimlik doğrulama

`/register`, `/authenticate` ve `/refreshToken` dışındaki bütün uç noktalar token ister.

1. Kullanıcı oluşturun: `POST /register`
2. Token alın: `POST /authenticate`
3. Diğer isteklerde başlığa ekleyin: `Authorization: Bearer <accessToken>`
4. Access token 2 saat, refresh token 4 saat geçerlidir. Süresi dolan access token'ı `POST /refreshToken` ile yenileyin.

```json
// POST /register ve POST /authenticate
{ "username": "kullanici", "password": "sifre" }

// POST /authenticate cevabı
{ "status": 200, "payload": { "accessToken": "...", "refreshToken": "..." }, "errorMessage": null }

// POST /refreshToken
{ "refreshToken": "..." }
```

## Uç noktalar

Her kaynak aynı yapıyı kullanır (`{kaynak}` yerine aşağıdaki tablodaki yolu yazın):

| Metot | Yol | İşlem |
|---|---|---|
| POST | `/rest/api/{kaynak}/save` | Kayıt oluşturur |
| GET | `/rest/api/{kaynak}/list` | Tüm kayıtları listeler |
| GET | `/rest/api/{kaynak}/list/{id}` | Id ile kayıt getirir |
| PUT | `/rest/api/{kaynak}/update/{id}` | Kaydı günceller |
| DELETE | `/rest/api/{kaynak}/delete/{id}` | Kaydı siler |

| Kaynak | Yol | İstek gövdesi |
|---|---|---|
| Adres | `address` | `city`, `district`, `neighborhood`, `street` |
| Hesap | `account` | `accountNo`, `iban`, `amount`, `currencyType` (`TL`, `USD`) |
| Müşteri | `customer` | `firstName`, `lastName`, `tckn`, `birthOfDate`, `addressId`, `accountId` |
| Galerici | `gallerist` | `firstName`, `lastName`, `addressId` |
| Araç | `car` | `plaka`, `brand`, `model`, `productionYear`, `price`, `currencyType`, `damagePrice`, `carStatusType` (`SALABLE`, `SALED`) |
| Galerici-Araç | `gallerist-car` | `galleristId`, `carId` |

İstek gövdesindeki bütün alanlar zorunludur.

Örnek, araç kaydı:

```json
// POST /rest/api/car/save
{
  "plaka": "34ABC123",
  "brand": "Toyota",
  "model": "Corolla",
  "productionYear": 2020,
  "price": 850000,
  "currencyType": "TL",
  "damagePrice": 0,
  "carStatusType": "SALABLE"
}
```

### Araç satışı

Bir müşterinin bir galerideki aracı satın almasını sağlar.

| Metot | Yol | İşlem |
|---|---|---|
| POST | `/rest/api/saled-car/save` | Satın alma yapar (`galleristId`, `carId`, `customerId`) |
| GET | `/rest/api/saled-car/list` | Bütün satışları listeler |
| GET | `/rest/api/saled-car/list/{id}` | Id ile satış getirir |

Satın alma sırasında:

1. Galerici, araç ve müşteri bulunur; araç o galeride olmalı ve durumu `SALABLE` olmalıdır.
2. Araç fiyatı müşterinin hesabının para birimine TCMB kurlarıyla çevrilir. Müşterinin alması gereken döviz için **döviz satış**, elindeki dövizle ödediği durumda **döviz alış** kuru kullanılır. Para birimleri aynıysa çevrim yapılmaz.
3. Bakiye yetmezse satış yapılmaz.
4. Tutar hesaptan düşülür, aracın durumu `SALED` olur ve satış kaydı (`paidAmount`, `paidCurrencyType`, `exchangeRate`) oluşturulur. Bunların hepsi tek bir transaction içinde yapılır.

Örnek: TL hesaplı bir müşteri 20.000 USD'lik aracı, USD döviz satış kuru 50,0489 iken 1.000.978,00 TL ödeyerek alır.

### Döviz kurları

TCMB'nin günlük kurlarını (`https://www.tcmb.gov.tr/kurlar/today.xml`) her istekte çekip TL karşılığı olarak döner. API anahtarı gerekmez.

| Metot | Yol | İşlem |
|---|---|---|
| GET | `/rest/api/currency-rates` | Günün tarihini ve bütün kurları döner |
| GET | `/rest/api/currency-rates/{kod}` | Tek bir kuru döner (ör. `USD`, `EUR`; büyük/küçük harf fark etmez) |

```json
// GET /rest/api/currency-rates/USD
{
  "status": 200,
  "payload": {
    "currencyCode": "USD",
    "name": "ABD DOLARI",
    "unit": 1,
    "forexBuying": 49.9589,
    "forexSelling": 50.0489,
    "banknoteBuying": 49.9239,
    "banknoteSelling": 50.1239
  },
  "errorMessage": null
}
```

TCMB'nin boş bıraktığı değerler `null` döner (ör. `XDR` için efektif kurlar). Bilinmeyen kodlarda `1004`, TCMB'ye ulaşılamazsa `1011` hatası döner.

### Notlar

Müşteri ve galerici kaydetmeden önce adres (ve müşteri için hesap) oluşturulmalıdır; cevapta bu kayıtlar iç içe döner. Bir adres en fazla bir müşteriye ve en fazla bir galericiye bağlanabilir (`@OneToOne`). Aynı galerici-araç çifti iki kez eklenemez.

## Cevap formatı

Başarılı cevaplar `RootEntity` ile sarılır:

```json
{ "status": 200, "payload": { ... }, "errorMessage": null }
```

Hatalar `GlobalExceptionHandler` üzerinden döner:

```json
{
  "status": 500,
  "exception": {
    "path": "/rest/api/car/list/99",
    "createTime": "...",
    "hostName": "...",
    "message": "kayıt bulunamadı : 99"
  }
}
```

Doğrulama hatalarında (eksik alan) `message` alan adına göre hata listesi içerir.

| Kod | Mesaj |
|---|---|
| 1004 | kayıt bulunamadı |
| 1005 | Token süresi bitmiştir. |
| 1006 | Kullanıcı Bulunamadı |
| 1007 | Kullanıcı Adı yada şifre hatalı |
| 1008 | Refresh Token Bulunamadı |
| 1009 | Refresh Token süresi bitmiştir. |
| 1010 | kayıt zaten mevcut |
| 1011 | döviz kurları alınamadı |
| 1012 | araç zaten satılmış |
| 1013 | araç bu galeride bulunmuyor |
| 1014 | müşterinin bakiyesi yetersiz |
| 1015 | müşterinin hesabı yok |
| 9999 | genel bir hata oluştu |

## Proje yapısı

```
src/main/java/com/example
├── config        Güvenlik ve uygulama ayarları
├── controller    REST arayüzleri ve impl/ altında uygulamaları
├── dto           İstek (…IU) ve cevap (Dto…) nesneleri
├── enums         CurrencyType, CarStatusType
├── exception     BaseException, ErrorMessage, MessageType
├── handler       Global hata yakalayıcı
├── jwt           Token üretimi ve doğrulama filtresi
├── model         JPA entity'leri
├── repository    Spring Data JPA repository'leri
├── service       Servis arayüzleri ve impl/ altında uygulamaları
└── starter       GalleristApplication (giriş noktası)
```
