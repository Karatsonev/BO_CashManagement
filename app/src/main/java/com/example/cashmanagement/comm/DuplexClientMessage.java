package com.example.cashmanagement.comm;

/**
 * Нов начин за предаване на сигурни и криптирани данни.
 * Двата елемента са RSA криптиран сметричен ключ и самото AES криптирано съобщение.
 * Created by christo.christov on 13.6.2017 г..
 */

public class DuplexClientMessage {
    public String salt;
    public String pepper;
}
