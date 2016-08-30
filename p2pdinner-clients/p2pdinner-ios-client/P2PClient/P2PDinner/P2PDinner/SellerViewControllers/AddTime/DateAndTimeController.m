//
//  DateAndTimeController.m
//  DatePicker
//
//  Created by Selvam M on 8/26/16.
//  Copyright © 2016 Selvam M. All rights reserved.
//

#import "DateAndTimeController.h"

@implementation DateAndTimeController
+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed{
    [self selectDateAction:pickerType withPresentViewController:viewController completionAction:completed withMinimumDate:[NSDate date]];
 }
+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed withMinimumDate:(NSDate *)minimumDate{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:nil message:@"\n\n\n\n\n\n\n\n\n" preferredStyle:UIAlertControllerStyleActionSheet];
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
    [alertController.view addSubview:picker];
    [alertController addAction:({
        UIAlertAction *action = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
            completed(picker.date);
        }];
        action;
    })];
    //    UIPopoverPresentationController *popoverController = alertController.popoverPresentationController;
    //    popoverController.sourceView = sender;
    //    popoverController.sourceRect = [sender bounds];
    [viewController presentViewController:alertController  animated:YES completion:nil];

}
@end
