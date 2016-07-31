//
//  MinimumOrdersCell.m
//  P2PDinner
//
//  Created by Selvam M on 4/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "MinimumOrdersCell.h"
#import "ItemDetailsShared.h"
typedef enum{
    DecrementValue,
    IncrementValue
    
}ChangeValue;
@implementation MinimumOrdersCell
- (void)awakeFromNib {
    // Initialization code
}

- (int)stringToInteger:(NSString *)stringValue{
    return [stringValue intValue];
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSCharacterSet *numbersOnly = [NSCharacterSet characterSetWithCharactersInString:@"0123456789"];
    NSCharacterSet *characterSetFromTextField = [NSCharacterSet characterSetWithCharactersInString:textField.text];
    
    BOOL stringIsValid = [numbersOnly isSupersetOfSet:characterSetFromTextField];
    return stringIsValid;
}
- (void)setMaximuOrdersValue:(NSNumber *)value{
     self.maximumTextField.text=[NSString stringWithFormat:@"%ld",(long)[value integerValue]];
}
- (NSNumber *)getMaximuOrdersValue{
    return [NSNumber numberWithDouble:[self.maximumTextField.text doubleValue]];
}

@end
