//
//  DateAndTimeController.m
//  DatePicker
//
//  Created by Selvam M on 8/26/16.
//  Copyright © 2016 Selvam M. All rights reserved.
//

#import "DateAndTimeController.h"
#import "StringConstants.h"


@implementation DateAndTimeController
+ (NSDate *)endOfDay
{
    NSCalendar *cal = [NSCalendar currentCalendar];
    NSDateComponents *components = [cal components:(  NSCalendarUnitMonth | NSCalendarUnitYear | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond ) fromDate:[NSDate date]];
    
    [components setHour:23];
    [components setMinute:59];
    [components setSecond:59];
    
    return [cal dateFromComponents:components];
    
}

+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed{
    [self selectDateAction:pickerType withPresentViewController:viewController completionAction:completed withMinimumDate:[NSDate date]];
 }
+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed withMinimumDate:(NSDate *)minimumDate{
    [self selectDateAction:pickerType withPresentViewController:viewController completionAction:completed withMinimumDate:minimumDate currentDate:[NSDate date]];
}
+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed withMinimumDate:(NSDate *)minimumDate currentDate:(NSDate *)currentDate{
    [self selectDateAction:pickerType withPresentViewController:viewController completionAction:completed withMinimumDate:minimumDate  withMaximumDate:[self endOfDay] currentDate:currentDate];

}
+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed withMinimumDate:(NSDate *)minimumDate withMaximumDate:(NSDate *)maximumDate currentDate:(NSDate *)currentDate{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:nil message:kAleartContent preferredStyle:UIAlertControllerStyleActionSheet];
    UIDatePicker *picker = [[UIDatePicker alloc] init];
    picker.center=CGPointMake(alertController.view.center.x, picker.center.y);
    if (pickerType==PickerSelectDate) {
        [picker setDatePickerMode:UIDatePickerModeDate];
    }else
    {
        [picker setDatePickerMode:UIDatePickerModeTime];
    }
    picker.minuteInterval=kMinuteDatePickerInterval;
    [picker setMinimumDate: minimumDate];
    [picker setMaximumDate:maximumDate];
    [picker setDate:currentDate];
    [alertController.view addSubview:picker];
    [alertController addAction:({
        UIAlertAction *action = [UIAlertAction actionWithTitle:kOK style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
            completed(picker.date);
        }];
        action;
    })];
    [viewController presentViewController:alertController  animated:YES completion:nil];
}
@end
