import { Injectable } from '@angular/core';
import { DateAdapter, MAT_DATE_FORMATS, MatDateFormats } from '@angular/material/core';
import moment from 'moment';
import { Moment } from 'moment';

@Injectable({
    providedIn: 'root'
  })
export class CustomDateAdapter extends DateAdapter<Moment> {
    private localeData = moment.localeData();

    getYear(date: Moment): number {
      return date.year();
    }
  
    getMonth(date: Moment): number {
      return date.month();
    }
  
    getDate(date: Moment): number {
      return date.date();
    }
  
    getDayOfWeek(date: Moment): number {
      return date.day();
    }
  
    getMonthNames(style: 'long' | 'short' | 'narrow'): string[] {
      return this.localeData.months();
    }
  
    getDateNames(): string[] {
      return this.localeData.weekdays();
    }
  
    getDayOfWeekNames(style: 'long' | 'short' | 'narrow'): string[] {
      return this.localeData.weekdays();
    }
  
    getYearName(date: Moment): string {
      return date.format('YYYY');
    }
  
    getFirstDayOfWeek(): number {
      return this.localeData.firstDayOfWeek();
    }
  
    getNumDaysInMonth(date: Moment): number {
      return date.daysInMonth();
    }
  
    clone(date: Moment): Moment {
      return date.clone();
    }
  
    createDate(year: number, month: number, date: number): Moment {
      return moment({ year, month, date });
    }
  
    today(): Moment {
      return moment();
    }
  
    parse(value: any, parseFormat: string | string[]): Moment | null {
      if (value && typeof value === 'string') {
        return moment(value, parseFormat, true);
      }
      return value ? moment(value) : null;
    }
  
    format(date: Moment, displayFormat: string): string {
      return date.format(displayFormat);
    }
  
    addCalendarYears(date: Moment, years: number): Moment {
      return date.add(years, 'years');
    }
  
    addCalendarMonths(date: Moment, months: number): Moment {
      return date.add(months, 'months');
    }
  
    addCalendarDays(date: Moment, days: number): Moment {
      return date.add(days, 'days');
    }
  
    toIso8601(date: Moment): string {
      return date.toISOString();
    }
  
    fromIso8601(isoString: string): Moment | null {
      return isoString ? moment(isoString, moment.ISO_8601, true) : null;
    }
  
    isDateInstance(obj: any): boolean {
      return moment.isMoment(obj);
    }
  
    isValid(date: Moment): boolean {
      return date.isValid();
    }
  
    invalid(): Moment {
      return moment.invalid();
    }
  
    override deserialize(value: any): Moment | null {
      let date;
      if (value instanceof Date) {
        date = moment(value);
      } else if (typeof value === 'string') {
        date = moment(value, moment.ISO_8601, true);
      } else if (moment.isMoment(value)) {
        return value;
      }
      if (date && this.isValid(date)) {
        return date;
      }
      return this.invalid();
    }
  
    override setLocale(locale: string) {
      super.setLocale(locale);
      this.localeData = moment.localeData(locale);
    }
  }
  
  export const CUSTOM_DATE_FORMATS = {
    parse: {
      dateInput: 'YYYY-MM-DDTHH:mm:ss.SSSZ',
    },
    display: {
      dateInput: 'YYYY-MM-DDTHH:mm:ss.SSSZ',
      monthYearLabel: 'MMM YYYY',
      dateA11yLabel: 'LL',
      monthYearA11yLabel: 'MMMM YYYY',
    },
  };