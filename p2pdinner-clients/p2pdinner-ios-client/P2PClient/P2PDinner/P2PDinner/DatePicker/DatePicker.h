//
//  DatePicker.h
//  P2PDinner
//
//  Created by selvam on 2/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol DatePickerDelegate<NSObject>;
- (void)selectedDate:(NSDate *)selectedDateValue;
@end
@interface DatePicker : UIViewController
{
    IBOutlet UILabel *selectedLable;
    IBOutlet UIDatePicker *datePicker;
    id <DatePickerDelegate> delegate;
}
@property(nonatomic,retain)id <DatePickerDelegate> delegate;
@property(nonatomic,strong)NSDate *selectedDate;
- (IBAction)setValueToLable:(id)sender;
- (IBAction)DoneButton:(id)sender;
- (NSDate *)selectedDate;
@end
