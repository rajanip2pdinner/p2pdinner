//
//  DatePicker.m
//  P2PDinner
//
//  Created by selvam on 2/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "DatePicker.h"
#import "Utility.h"

@interface DatePicker ()

@end

@implementation DatePicker
@synthesize delegate;

- (void)viewDidLoad {
    if ([_selectedDate timeIntervalSinceNow] <= 0.0) {
        [datePicker setMinimumDate:_selectedDate];
    }else
        [datePicker setMinimumDate: [NSDate date]];
    [self setValueToLable:datePicker];
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)DoneButton:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
    [self.delegate selectedDate:[datePicker date]];
}

- (IBAction)setValueToLable:(id)sender{
    selectedLable.text=[Utility dateToStringFormat:[datePicker date]timeZone:LOCAL];
}

- (NSDate *)selectedDate{
    return [datePicker date];
}
/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
