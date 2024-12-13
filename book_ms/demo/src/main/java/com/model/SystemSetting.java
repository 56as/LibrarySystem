package com.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "system_settings")
public class SystemSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "设置键不能为空")
    @Column(unique = true, nullable = false)
    private String settingKey;

    private String settingValue;

    private String description;

    // 预定义的设置键
    public static final String MAX_BORROWINGS_PER_READER = "max_borrowings_per_reader";
    public static final String DEFAULT_BORROWING_DAYS = "default_borrowing_days";
    public static final String MAX_RENEWALS = "max_renewals";
    public static final String OVERDUE_FINE_PER_DAY = "overdue_fine_per_day";
    public static final String EMAIL_NOTIFICATION_ENABLED = "email_notification_enabled";
    public static final String DUE_REMINDER_DAYS = "due_reminder_days";

    // 邮件设置
    public static final String SMTP_HOST = "smtp_host";
    public static final String SMTP_PORT = "smtp_port";
    public static final String SMTP_USERNAME = "smtp_username";
    public static final String SMTP_PASSWORD = "smtp_password";
    public static final String MAIL_FROM = "mail_from";

    @Version
    private Long version;
} 