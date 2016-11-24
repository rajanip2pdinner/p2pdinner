//
//  CostCell.m
//  P2PDinner
//
//  Created by Selvam M on 4/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CostCell.h"
#import "Utility.h"

@implementation CostCell
- (double)getValueOfPriceLableVlaue:(NSString *)stringValue{
    NSString *valueWithDolor=[[stringValue componentsSeparatedByString:@" "] objectAtIndex:0];
    return [[valueWithDolor stringByReplacingOccurrencesOfString:@"$" withString:@""] doubleValue];
}


- (void)changePricePerMealValue:(ChangeValue)value{
    int changePriceIntValue=[self getValueOfPriceLableVlaue:pricePerMealLable.text];
    switch (value) {
        case IncrementValue:
        {
            if (changePriceIntValue<50) {
                changePriceIntValue++;
                pricePerMealLable.text=[NSString stringWithFormat:@"%d %@"
                                        ,changePriceIntValue,
                                        (changePriceIntValue==1)?@"dollars":@"dollars"];
            }
        }
            break;
        case DecrementValue:
        {
            if (changePriceIntValue>=1) {
                changePriceIntValue--;
                pricePerMealLable.text=[NSString stringWithFormat:@"%d %@",
                                        changePriceIntValue,
                                        (changePriceIntValue==1)?@"dollars":@"dollars"];
            }
        }
            break;
    }
}


- (void)setPrice:(NSNumber *)price{
    if (![Utility getLocalCurrencyName]||!([Utility getLocalCurrencyName].length>0)) {
            NSLocale* localPrice = [[NSLocale alloc] initWithLocaleIdentifier:[[NSLocale preferredLanguages] objectAtIndex:0]];
            NSNumberFormatter *fmtr = [[NSNumberFormatter alloc] init];
            [fmtr setNumberStyle:NSNumberFormatterCurrencyPluralStyle];
            [fmtr setLocale:localPrice];
            NSCharacterSet *numbersSet = [NSCharacterSet characterSetWithCharactersInString:@"0123456789."];
            NSString *trimmedString = [[fmtr stringFromNumber:price] stringByTrimmingCharactersInSet:numbersSet];
            trimmedString = [trimmedString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
            [pricePerMealLable setText:[NSString stringWithFormat:@"Cost Per Plate (%@)",trimmedString]];
    }else
            [pricePerMealLable setText:[NSString stringWithFormat:@"Cost Per Plate (%@)",[Utility getLocalCurrencyName]]];
    
    [self.pricePerMealTextField setText:[price stringValue]];
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSString *newString = [textField.text stringByReplacingCharactersInRange:range
                                                                  withString:string];
    NSArray  *arrayOfString = [newString componentsSeparatedByString:@"."];
    
    if ([arrayOfString count] > 2 )
            return NO;
    
    return YES;
}

- (NSNumber *)getCostForDinner{
    return [NSNumber numberWithDouble:[self.pricePerMealTextField.text doubleValue]];
}

@end
