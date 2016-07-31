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
- (void)awakeFromNib {
    // Initialization code
}

- (double)getValueOfPriceLableVlaue:(NSString *)stringValue{
    NSString *valueWithDolor=[[stringValue componentsSeparatedByString:@" "]objectAtIndex:0];
    return [[valueWithDolor stringByReplacingOccurrencesOfString:@"$" withString:@""] doubleValue];
}

//- (void)changepriceCentsSegmentValue:(ChangeValue)value{
//    double changePriceIntValue=[self getValueOfPriceLableVlaue:pricePerMealCentsLable.text];
//    switch (value) {
//        case IncrementValue:
//        {
//            changePriceIntValue=changePriceIntValue+25;
//            if (changePriceIntValue>=100) {
//                changePriceIntValue=changePriceIntValue-100;
//            }
//            
//            pricePerMealCentsLable.text=[NSString stringWithFormat:@"%.2d cents",(int)changePriceIntValue];
//        }
//            break;
//            
//        case DecrementValue:
//        {
//            if (changePriceIntValue<=0) {
//                changePriceIntValue=100;
//            }
//            changePriceIntValue=changePriceIntValue-25;
//            pricePerMealCentsLable.text=[NSString stringWithFormat:@"%.2d cents",(int)changePriceIntValue];
//        }
//            break;
//    }
//}

- (void)changePricePerMealValue:(ChangeValue)value{
    int changePriceIntValue=[self getValueOfPriceLableVlaue:pricePerMealLable.text];
    switch (value) {
        case IncrementValue:
        {
            if (changePriceIntValue<50) {
                changePriceIntValue++;
                pricePerMealLable.text=[NSString stringWithFormat:@"%d %@",changePriceIntValue,(changePriceIntValue==1)?@"dollars":@"dollars"];
            }
        }
            break;
            
        case DecrementValue:
        {
            if (changePriceIntValue>=1) {
                changePriceIntValue--;
                pricePerMealLable.text=[NSString stringWithFormat:@"%d %@",changePriceIntValue,(changePriceIntValue==1)?@"dollars":@"dollars"];
            }
        }
            break;
    }
}

//- (IBAction)pricePerMealSegmentAction:(id)sender{
//    UISegmentedControl *segment=(UISegmentedControl *)sender;
//    if (segment.selectedSegmentIndex==0) {
//        [self changePricePerMealValue:DecrementValue];
//    }else{
//        [self changePricePerMealValue:IncrementValue];
//    }
//    
//    [self performSelector:@selector(removeSelectedIndex:) withObject:sender afterDelay:0.1];
//}
//
//- (IBAction)priceCentsSegmentAction:(id)sender{
//    UISegmentedControl *segment=(UISegmentedControl *)sender;
//    if (segment.selectedSegmentIndex==0) {
//        [self changepriceCentsSegmentValue:DecrementValue];
//    }else{
//        [self changepriceCentsSegmentValue:IncrementValue];
//    }
//    
//    [self performSelector:@selector(removeSelectedIndex:) withObject:sender afterDelay:0.1];
//}
//#pragma args Default unSelect optionin SegmentContorl
//- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
//    [super setSelected:selected animated:animated];
//}
//
//- (void)removeSelectedIndex:(UISegmentedControl *)segmentBtn{
//    [segmentBtn setSelectedSegmentIndex:-1];
//    [self.delegate updatedItems];
//}
//#pragma args setValuetoTextField

- (void)setPrice:(NSNumber *)price{
    if (![Utility getLocalCurrencyName]||!([Utility getLocalCurrencyName].length>0)) {
        NSLocale* localPrice = [[NSLocale alloc] initWithLocaleIdentifier:[[NSLocale preferredLanguages] objectAtIndex:0]];
        NSNumberFormatter *fmtr = [[NSNumberFormatter alloc] init];
        [fmtr setNumberStyle:NSNumberFormatterCurrencyPluralStyle];
        [fmtr setLocale:localPrice];
        NSCharacterSet *numbersSet = [NSCharacterSet characterSetWithCharactersInString:@"0123456789."];
        NSString *trimmedString = [[fmtr stringFromNumber:price] stringByTrimmingCharactersInSet:numbersSet];
        trimmedString = [trimmedString stringByTrimmingCharactersInSet:
                         [NSCharacterSet whitespaceAndNewlineCharacterSet]];
        [pricePerMealLable setText:[NSString stringWithFormat:@"Cost Per Plate (%@)",trimmedString]];
    }else
        [pricePerMealLable setText:[NSString stringWithFormat:@"Cost Per Plate (%@)",[Utility getLocalCurrencyName]]];
    [self.pricePerMealTextField setText:[price stringValue]];
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSString *newString = [textField.text stringByReplacingCharactersInRange:range withString:string];
    NSArray  *arrayOfString = [newString componentsSeparatedByString:@"."];
    
    if ([arrayOfString count] > 2 )
        return NO;
    
    return YES;
}

- (NSNumber *)getCostForDinner{
    return [NSNumber numberWithDouble:[self.pricePerMealTextField.text doubleValue]];
}

@end
