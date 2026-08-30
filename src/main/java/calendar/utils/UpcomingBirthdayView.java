package calendar.utils;

import net.dv8tion.jda.api.entities.Member;

import java.time.Month;

public record UpcomingBirthdayView(int day, String month, String targetName) {
}
