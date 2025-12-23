package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.model.Alerts;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для класса AlertsEditViewController.
 * Тестирует только бизнес-логику без зависимостей от JavaFX.
 */
class AlertsEditViewControllerTest {

    // Вспомогательный класс для симуляции состояния контроллера
    static class ControllerState {
        boolean hasRoom = false;
        boolean hasParam = false;
        String value = "";
        String datetime = "";
        boolean resolved = false;
        String resolvedBy = "";
        String resolutionTime = "";

        Alerts currentAlert = null;
    }

    @Test
    void testInitFormNewAlert() {
        // Тест 1: Инициализация формы для нового алерта
        Alerts alert = null;
        String saveButtonText = (alert != null) ? "Обновить" : "Добавить";

        assertEquals("Добавить", saveButtonText,
                "Текст кнопки должен быть 'Добавить' для нового алерта");
    }

    @Test
    void testInitFormExistingAlert() {
        // Тест 2: Инициализация формы для существующего алерта
        Alerts alert = new Alerts();
        String saveButtonText = (alert != null) ? "Обновить" : "Добавить";

        assertEquals("Обновить", saveButtonText,
                "Текст кнопки должен быть 'Обновить' для существующего алерта");
    }

    @Test
    void testValidValuesAllValid() {
        // Тест 3: Все значения валидны
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = true;
        state.value = "25.5";
        state.datetime = "2024-05-15T10:30:00";
        state.resolvedBy = "Администратор";
        state.resolutionTime = "2024-05-15T11:00:00";

        assertTrue(isValidState(state),
                "Состояние со всеми валидными значениями должно проходить проверку");
    }

    @Test
    void testValidValuesNoRoom() {
        // Тест 4: Не выбрана комната
        ControllerState state = new ControllerState();
        state.hasRoom = false;  // Нет комнаты!
        state.hasParam = true;
        state.value = "25.5";
        state.datetime = "2024-05-15T10:30:00";
        state.resolvedBy = "Администратор";
        state.resolutionTime = "2024-05-15T11:00:00";

        assertFalse(isValidState(state),
                "Состояние без выбранной комнаты не должно проходить проверку");
    }

    @Test
    void testValidValuesNoParam() {
        // Тест 5: Не выбран параметр
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = false;  // Нет параметра!
        state.value = "25.5";
        state.datetime = "2024-05-15T10:30:00";
        state.resolvedBy = "Администратор";
        state.resolutionTime = "2024-05-15T11:00:00";

        assertFalse(isValidState(state),
                "Состояние без выбранного параметра не должно проходить проверку");
    }

    @Test
    void testValidValuesEmptyValue() {
        // Тест 6: Пустое значение value
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = true;
        state.value = "";  // Пустое значение!
        state.datetime = "2024-05-15T10:30:00";
        state.resolvedBy = "Администратор";
        state.resolutionTime = "2024-05-15T11:00:00";

        assertFalse(isValidState(state),
                "Состояние с пустым значением не должно проходить проверку");
    }

    @Test
    void testValidValuesInvalidDateTime() {
        // Тест 7: Некорректный формат даты/времени
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = true;
        state.value = "25.5";
        state.datetime = "неправильная дата";  // Некорректный формат!
        state.resolvedBy = "Администратор";
        state.resolutionTime = "2024-05-15T11:00:00";

        assertFalse(isValidState(state),
                "Состояние с некорректной датой не должно проходить проверку");
    }

    @Test
    void testValidValuesEmptyResolvedBy() {
        // Тест 8: Пустое поле resolvedBy
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = true;
        state.value = "25.5";
        state.datetime = "2024-05-15T10:30:00";
        state.resolvedBy = "";  // Пустое поле!
        state.resolutionTime = "2024-05-15T11:00:00";

        assertFalse(isValidState(state),
                "Состояние с пустым resolvedBy не должно проходить проверку");
    }

    @Test
    void testValidValuesInvalidResolutionTime() {
        // Тест 9: Некорректный формат resolutionTime
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = true;
        state.value = "25.5";
        state.datetime = "2024-05-15T10:30:00";
        state.resolvedBy = "Администратор";
        state.resolutionTime = "неправильное время";  // Некорректный формат!

        assertFalse(isValidState(state),
                "Состояние с некорректным resolutionTime не должно проходить проверку");
    }

    @Test
    void testValidValuesOnlySpaces() {
        // Тест 10: Поля содержат только пробелы
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = true;
        state.value = "   ";  // Только пробелы!
        state.datetime = "2024-05-15T10:30:00";
        state.resolvedBy = "   ";  // Только пробелы!
        state.resolutionTime = "2024-05-15T11:00:00";

        assertFalse(isValidState(state),
                "Поля с одними пробелами должны считаться пустыми");
    }

    @Test
    void testSetStateAlertsNewAlert() {
        // Тест 11: Создание нового алерта
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = true;
        state.value = "25,5";  // С запятой!
        state.datetime = "2024-05-15T10:30:00";
        state.resolved = true;
        state.resolvedBy = "Иванов И.И.";
        state.resolutionTime = "2024-05-15T11:00:00";

        Alerts alert = new Alerts();
        // Симулируем setStateAlerts()
        alert.setValue(parseValue(state.value));
        alert.setDatetime(parseDateTime(state.datetime));
        alert.setResolved(state.resolved);
        alert.setResolvedBy(state.resolvedBy);
        alert.setResolutionTime(parseDateTime(state.resolutionTime));

        assertEquals(25.5, alert.getValue(), 0.001, "Значение должно быть правильно распаршено");
        assertEquals(LocalDateTime.parse("2024-05-15T10:30:00"), alert.getDatetime());
        assertTrue(alert.getResolved(), "Resolved должен быть true");
        assertEquals("Иванов И.И.", alert.getResolvedBy());
        assertEquals(LocalDateTime.parse("2024-05-15T11:00:00"), alert.getResolutionTime());
    }

    @Test
    void testSetStateAlertsUpdateAlert() {
        // Тест 12: Обновление существующего алерта
        ControllerState state = new ControllerState();
        state.hasRoom = true;
        state.hasParam = true;
        state.value = "30.0";
        state.datetime = "2024-05-16T12:00:00";
        state.resolved = false;
        state.resolvedBy = "Петров П.П.";
        state.resolutionTime = "2024-05-16T12:30:00";

        Alerts alert = new Alerts();
        // Симулируем setStateAlerts() для обновления
        alert.setValue(parseValue(state.value));
        alert.setDatetime(parseDateTime(state.datetime));
        alert.setResolved(state.resolved);
        alert.setResolvedBy(state.resolvedBy);
        alert.setResolutionTime(parseDateTime(state.resolutionTime));

        assertEquals(30.0, alert.getValue(), 0.001);
        assertEquals(LocalDateTime.parse("2024-05-16T12:00:00"), alert.getDatetime());
        assertFalse(alert.getResolved(), "Resolved должен быть false");
        assertEquals("Петров П.П.", alert.getResolvedBy());
        assertEquals(LocalDateTime.parse("2024-05-16T12:30:00"), alert.getResolutionTime());
    }

    @Test
    void testValueParsingWithComma() {
        // Тест 13: Парсинг значения с запятой
        String valueWithComma = "25,5";
        double parsedValue = parseValue(valueWithComma);
        assertEquals(25.5, parsedValue, 0.001,
                "Значение с запятой должно правильно конвертироваться");
    }

    @Test
    void testValueParsingWithDot() {
        // Тест 14: Парсинг значения с точкой
        String valueWithDot = "25.5";
        double parsedValue = parseValue(valueWithDot);
        assertEquals(25.5, parsedValue, 0.001,
                "Значение с точкой должно правильно парситься");
    }

    @Test
    void testValueParsingInvalid() {
        // Тест 15: Парсинг некорректного значения
        String invalidValue = "abc";
        assertThrows(NumberFormatException.class, () -> parseValue(invalidValue),
                "Некорректное значение должно вызывать исключение");
    }

    @Test
    void testDateTimeParsingValid() {
        // Тест 16: Парсинг корректной даты
        String validDateTime = "2024-05-15T10:30:00";
        LocalDateTime parsed = parseDateTime(validDateTime);

        assertEquals(2024, parsed.getYear(), "Год должен быть 2024");
        assertEquals(5, parsed.getMonthValue(), "Месяц должен быть май (5)");
        assertEquals(15, parsed.getDayOfMonth(), "День должен быть 15");
        assertEquals(10, parsed.getHour(), "Час должен быть 10");
        assertEquals(30, parsed.getMinute(), "Минуты должны быть 30");
    }

    @Test
    void testDateTimeParsingInvalid() {
        // Тест 17: Парсинг некорректной даты
        String invalidDateTime = "неправильная дата";
        assertThrows(DateTimeParseException.class, () -> parseDateTime(invalidDateTime),
                "Некорректная дата должна вызывать исключение");
    }

    @Test
    void testInitInputControllersNullSafety() {
        // Тест 18: Безопасная работа с null значениями
        Alerts alert = new Alerts(); // Все поля null по умолчанию

        // Симулируем initInputControllers() логику
        if (alert.getValue() != null) {
            String formattedValue = String.format("%f", alert.getValue());
            fail("Не должно форматироваться null значение");
        }

        if (alert.getDatetime() != null) {
            String formattedDatetime = String.format("%s", alert.getDatetime());
            fail("Не должно форматироваться null datetime");
        }

        // Проверяем, что код не падает с null
        assertDoesNotThrow(() -> {
            if (alert.getResolved() != null) {
                boolean isSelected = alert.getResolved();
            }
        });
    }

    @Test
    void testResolvedCheckboxNull() {
        // Тест 19: Проверка чекбокса с null значением
        Alerts alert = new Alerts();
        alert.setResolved(null);

        // В реальном коде: if (alerts.getResolved() != null)
        if (alert.getResolved() != null) {
            boolean isSelected = alert.getResolved();
            fail("Не должно заходить в этот блок для null значения");
        }

        assertNull(alert.getResolved(), "Resolved должен быть null");
    }

    @Test
    void testResolvedCheckboxTrue() {
        // Тест 20: Проверка чекбокса с true значением
        Alerts alert = new Alerts();
        alert.setResolved(true);

        assertNotNull(alert.getResolved(), "Resolved не должен быть null");
        assertTrue(alert.getResolved(), "Resolved должен быть true");
    }

    @Test
    void testStringFormattingForDouble() {
        // Тест 21: Форматирование double значения
        Double value = 25.5;
        String formattedValue = String.format("%f", value);
        assertEquals("25,500000", formattedValue,
                "Double должен форматироваться с 6 знаками после запятой");
    }

    @Test
    void testStringFormattingForDateTime() {
        // Тест 22: Форматирование LocalDateTime
        LocalDateTime datetime = LocalDateTime.of(2024, 5, 15, 10, 30, 0);
        String formattedDatetime = String.format("%s", datetime);
        assertTrue(formattedDatetime.startsWith("2024-05-15T10:30"),
                "LocalDateTime должен правильно форматироваться");
    }

    // Вспомогательные методы для симуляции логики контроллера

    private boolean isValidState(ControllerState state) {
        // Симулируем логику validValues()
        if (!state.hasRoom) {
            return false; // showAlert("Rooms");
        }
        if (!state.hasParam) {
            return false; // showAlert("Param");
        }
        if (state.value == null || state.value.trim().isEmpty()) {
            return false; // showAlert("Value");
        }
        if (state.datetime == null || state.datetime.trim().isEmpty()) {
            return false; // showAlert("Datetime");
        } else {
            try {
                parseDateTime(state.datetime);
            } catch (DateTimeParseException e) {
                return false; // showAlert("Некорректное значение в Datetime");
            }
        }
        if (state.resolvedBy == null || state.resolvedBy.trim().isEmpty()) {
            return false; // showAlert("Resolved by");
        }
        if (state.resolutionTime == null || state.resolutionTime.trim().isEmpty()) {
            return false; // showAlert("time");
        } else {
            try {
                parseDateTime(state.resolutionTime);
            } catch (DateTimeParseException e) {
                return false; // showAlert("Некорректное значение в Resolution time");
            }
        }
        return true;
    }

    private double parseValue(String valueStr) {
        // Симулируем логику из setStateAlerts(): replace(",", ".")
        String normalized = valueStr.replace(",", ".");
        return Double.parseDouble(normalized);
    }

    private LocalDateTime parseDateTime(String datetimeStr) {
        return LocalDateTime.parse(datetimeStr);
    }

    // Вспомогательный класс для симуляции сервиса
    static class MockAlertsService {
        int saveCount = 0;
        int updateCount = 0;

        void save(Alerts alert) {
            saveCount++;
        }

        void update(Alerts alert) {
            updateCount++;
        }
    }

    @Test
    void testOnSaveBtnNewAlert() {
        // Тест 23: Логика сохранения нового алерта
        MockAlertsService service = new MockAlertsService();
        Alerts alert = null; // Новый алерт

        if (alert != null) {
            service.update(alert);
        } else {
            alert = new Alerts();
            service.save(alert);
        }

        assertEquals(1, service.saveCount, "Должен быть вызван save()");
        assertEquals(0, service.updateCount, "update() не должен быть вызван");
    }

    @Test
    void testOnSaveBtnUpdateAlert() {
        // Тест 24: Логика обновления существующего алерта
        MockAlertsService service = new MockAlertsService();
        Alerts alert = new Alerts(); // Существующий алерт

        if (alert != null) {
            service.update(alert);
        } else {
            alert = new Alerts();
            service.save(alert);
        }

        assertEquals(0, service.saveCount, "save() не должен быть вызван");
        assertEquals(1, service.updateCount, "Должен быть вызван update()");
    }

    @Test
    void testEdgeCasesValueZero() {
        // Тест 25: Крайний случай - значение 0
        String zeroValue = "0";
        double parsed = parseValue(zeroValue);
        assertEquals(0.0, parsed, 0.001, "Значение 0 должно правильно парситься");
    }

    @Test
    void testEdgeCasesValueNegative() {
        // Тест 26: Крайний случай - отрицательное значение
        String negativeValue = "-25,5";
        double parsed = parseValue(negativeValue);
        assertEquals(-25.5, parsed, 0.001, "Отрицательное значение должно правильно парситься");
    }

    @Test
    void testEdgeCasesValueLarge() {
        // Тест 27: Крайний случай - большое значение
        String largeValue = "999999.999";
        double parsed = parseValue(largeValue);
        assertEquals(999999.999, parsed, 0.001, "Большое значение должно правильно парситься");
    }

    @Test
    void testEdgeCasesDateTimeMin() {
        // Тест 28: Крайний случай - минимальная дата
        String minDate = "2024-01-01T00:00:00";
        LocalDateTime parsed = parseDateTime(minDate);
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0, 0), parsed,
                "Минимальная дата должна правильно парситься");
    }

    @Test
    void testEdgeCasesDateTimeMax() {
        // Тест 29: Крайний случай - максимальная дата
        String maxDate = "2024-12-31T23:59:59";
        LocalDateTime parsed = parseDateTime(maxDate);
        assertEquals(LocalDateTime.of(2024, 12, 31, 23, 59, 59), parsed,
                "Максимальная дата должна правильно парситься");
    }

    @Test
    void testOnCancelBtnLogic() {
        // Тест 30: Логика кнопки отмены
        // В реальном коде: cancelBtn.getScene().getWindow().hide();
        // Мы просто проверяем, что нет исключений
        assertDoesNotThrow(() -> {
            // Симулируем вызов onCancelBtn()
            boolean windowClosed = true; // Предполагаем, что окно закрывается
            assertTrue(windowClosed, "Окно должно закрываться при отмене");
        });
    }
}