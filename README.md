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

`SaledCar` (satılan araç) entity'si modelde tanımlı, ancak henüz uç noktası yok.
