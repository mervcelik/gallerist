package com.example.service.impl;

import com.example.dto.DtoCurrencyRate;
import com.example.dto.DtoCurrencyRates;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.service.ICurrencyRatesService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Fetches the daily exchange rates published by the Central Bank of the Republic of Turkey (TCMB).
 * Rates are against TRY; no API key is required.
 */
@Service
public class CurrencyRatesServiceImpl implements ICurrencyRatesService {

    private static final String TCMB_URL = "https://www.tcmb.gov.tr/kurlar/today.xml";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();


    @Override
    public DtoCurrencyRates getCurrencyRates() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(TCMB_URL))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<byte[]> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BaseException(new ErrorMessage(MessageType.CURRENCY_RATES_NOT_AVAILABLE, e.toString()));
        } catch (Exception e) {
            throw new BaseException(new ErrorMessage(MessageType.CURRENCY_RATES_NOT_AVAILABLE, e.toString()));
        }

        if (response.statusCode() != 200) {
            throw new BaseException(new ErrorMessage(MessageType.CURRENCY_RATES_NOT_AVAILABLE, "TCMB HTTP " + response.statusCode()));
        }

        byte[] xml = response.body();
        if (xml == null || xml.length == 0) {
            throw new BaseException(new ErrorMessage(MessageType.CURRENCY_RATES_NOT_AVAILABLE, "boş cevap"));
        }

        return parse(xml);
    }

    @Override
    public DtoCurrencyRate getCurrencyRate(String currencyCode) {
        for (DtoCurrencyRate rate : getCurrencyRates().getRates()) {
            if (rate.getCurrencyCode().equalsIgnoreCase(currencyCode)) {
                return rate;
            }
        }
        throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, currencyCode));
    }

    private DtoCurrencyRates parse(byte[] xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml));
            Element root = document.getDocumentElement();

            List<DtoCurrencyRate> rates = new ArrayList<>();
            NodeList currencies = root.getElementsByTagName("Currency");

            for (int i = 0; i < currencies.getLength(); i++) {
                Element currency = (Element) currencies.item(i);

                DtoCurrencyRate rate = new DtoCurrencyRate();
                rate.setCurrencyCode(currency.getAttribute("CurrencyCode"));
                rate.setName(text(currency, "Isim"));
                rate.setUnit(Integer.valueOf(text(currency, "Unit")));
                rate.setForexBuying(decimal(currency, "ForexBuying"));
                rate.setForexSelling(decimal(currency, "ForexSelling"));
                rate.setBanknoteBuying(decimal(currency, "BanknoteBuying"));
                rate.setBanknoteSelling(decimal(currency, "BanknoteSelling"));

                rates.add(rate);
            }

            return new DtoCurrencyRates(root.getAttribute("Tarih"), rates);
        } catch (Exception e) {
            throw new BaseException(new ErrorMessage(MessageType.CURRENCY_RATES_NOT_AVAILABLE, e.toString()));
        }
    }

    private String text(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return null;
        }
        String value = nodes.item(0).getTextContent();
        return value == null || value.isBlank() ? null : value.trim();
    }

    private BigDecimal decimal(Element parent, String tagName) {
        String value = text(parent, tagName);
        return value == null ? null : new BigDecimal(value);
    }

}
