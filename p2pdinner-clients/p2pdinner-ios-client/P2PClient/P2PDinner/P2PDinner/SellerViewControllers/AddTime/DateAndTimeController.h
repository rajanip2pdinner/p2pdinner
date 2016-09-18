//
//  DateAndTimeController.h
//  DatePicker
//
//  Created by Selvam M on 8/26/16.
//  Copyright © 2016 Selvam M. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#define kMinuteDatePickerInterval 15
typedef NS_ENUM(NSInteger, PickerType) {
    PickerSelectDate,
    PickerSelectTime
};
@interface DateAndTimeController : NSObject

+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed;
+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed withMinimumDate:(NSDate *)minimumDate;
+(void)selectDateAction:(PickerType)pickerType withPresentViewController:(UIViewController *)viewController completionAction:(void (^)(NSDate *))completed withMinimumDate:(NSDate *)minimumDate currentDate:(NSDate *)currentDate;
@end
