//
//  CostCell.m
//  P2PDinner
//
//  Created by Selvam M on 4/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CostCell.h"

@implementation CostCell
- (void)awakeFromNib {
    // Initialization code
}

- (double)getValueOfPriceLableVlaue:(NSString *)stringValue{
    NSString *valueWithDolor=[[stringValue componentsSeparatedByString:@" "]objectAtIndex:0];
    return [[valueWithDolor stringByReplacingOccurrencesOfString:@"$" withString:@""] doubleValue];
}

- (void)changepriceCentsSegmentValue:(ChangeValue)value{
    double changePriceIntValue=[self getValueOfPriceLableVlaue:pricePerMealCentsLable.text];
    switch (value) {
        case IncrementValue:
        {
            changePriceIntValue=changePriceIntValue+25;
            if (changePriceIntValue>=100) {
                changePriceIntValue=changePriceIntValue-100;
            }
            
            pricePerMealCentsLable.text=[NSString stringWithFormat:@"%.2d cents",(int)changePriceIntValue];
        }
            break;
            
        case DecrementValue:
        {
            if (changePriceIntValue<=0) {
                changePriceIntValue=100;
            }
            changePriceIntValue=changePriceIntValue-25;
            pricePerMealCentsLable.text=[NSString stringWithFormat:@"%.2d cents",(int)changePriceIntValue];
        }
            break;
    }
}

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

- (IBAction)pricePerMealSegmentAction:(id)sender{
    UISegmentedControl *segment=(UISegmentedControl *)sender;
    if (segment.selectedSegmentIndex==0) {
        [self changePricePerMealValue:DecrementValue];
    }else{
        [self changePricePerMealValue:IncrementValue];
    }
    
    [self performSelector:@selector(removeSelectedIndex:) withObject:sender afterDelay:0.1];
}

- (IBAction)priceCentsSegmentAction:(id)sender{
    UISegmentedControl *segment=(UISegmentedControl *)sender;
    if (segment.selectedSegmentIndex==0) {
        [self changepriceCentsSegmentValue:DecrementValue];
    }else{
        [self changepriceCentsSegmentValue:IncrementValue];
    }
    
    [self performSelector:@selector(removeSelectedIndex:) withObject:sender afterDelay:0.1];
}
#pragma args Default unSelect optionin SegmentContorl
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}

- (void)removeSelectedIndex:(UISegmentedControl *)segmentBtn{
    [segmentBtn setSelectedSegmentIndex:-1];
    [self.delegate updatedItems];
}
#pragma args setValuetoTextField
- (void)setPrice:(NSNumber *)price{
    int dollorValue=[price intValue];
    float centValue=([price floatValue]-dollorValue)*100;
    pricePerMealLable.text=[NSString stringWithFormat:@"%d %@",dollorValue,(dollorValue==1)?@"dollars":@"dollars"];
    pricePerMealCentsLable.text=[NSString stringWithFormat:@"%.2d cents",(int)centValue];
}

- (NSNumber *)getCostForDinner{
    int dolor=[self getValueOfPriceLableVlaue:pricePerMealLable.text];
    double cents=[self getValueOfPriceLableVlaue:pricePerMealCentsLable.text];
    NSString *string=[NSString stringWithFormat:@"%d.%2d",dolor,(int)cents];
    return [NSNumber numberWithDouble:[string doubleValue]];
}

@end
